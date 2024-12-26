package onepick.kanban.user.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.user.dto.JwtAuthResponse;
import onepick.kanban.user.dto.LoginRequestDto;
import onepick.kanban.user.dto.UserRequestDto;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.util.AuthenticationScheme;
import onepick.kanban.util.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(UserRequestDto userRequestDto) {
        boolean isExist = userRepository.existsByEmail(userRequestDto.getEmail());

        if (isExist) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        userRequestDto.updatePassword(encodedPassword);

        userRepository.save(userRequestDto.toEntity());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        user.updateIsDeleted();
        userRepository.save(user);
    }

    public JwtAuthResponse login(LoginRequestDto loginRequestDto) {
        Optional<User> user = userRepository.findByEmail(loginRequestDto.getEmail());

        if (user.isEmpty() || !passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 이메일 혹은 잘못된 비밀번호 입니다.");
        }

        // 사용자 인증 후 인증 객체를 저장
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = this.jwtProvider.generateToken(authentication);

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken);
    }
}
