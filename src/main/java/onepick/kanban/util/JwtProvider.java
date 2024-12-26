package onepick.kanban.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.expiry-millis}")
    private long expiryMillis;

    private final UserRepository userRepository;

    public String generateToken(Authentication authentication) throws EntityNotFoundException {
        String username = authentication.getName();
        return this.generateTokenBy(username);
    }

    public String tempToken(Authentication authentication) throws EntityNotFoundException {
        String username = authentication.getName();
        return this.tempTokenBy(username);
    }

    public String getUsername(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    // 토큰이 유효한지 확인
    public boolean validToken(String token) throws JwtException {
        try {
            return !this.tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰입니다.: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료되었습니다.: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.: {}", e.getMessage());
        }

        return false;
    }

    // 이메일 주소를 이용해 토큰을 생성한 후 반환
    private String generateTokenBy(String email) throws EntityNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("유효하지 않은 이메일 입니다.");
        }

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + this.expiryMillis);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("role", user.get().getRole())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    // 이메일 주소를 이용해 임시 토큰 생성한 후 반환
    private String tempTokenBy(String email) throws EntityNotFoundException {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + 60000);

        return Jwts.builder()
                .subject(email)
                .claim("type", "temp")
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    // 임시 토큰 검증
    public boolean isTempToken(String token) {
        Claims claims = getClaims(token);
        System.out.println(claims.get("type"));
        return "temp".equals(claims.get("type"));
    }

    // JWT의 claim 부분을 추출
    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어 있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 입력받은 토큰의 만료 여부 (true: 만료)
    private boolean tokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 입력 받은 토큰의 만료일을 반환
    private Date getExpirationDateFromToken(String token) {
        return this.resolveClaims(token, Claims::getExpiration);
    }

    // 토큰에 입력 받은 로직을 적용하고 그 결과를 반환
    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaims(token);
        return claimsResolver.apply(claims);
    }
}
