package onepick.kanban.user.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.dto.JwtAuthResponse;
import onepick.kanban.user.dto.LoginRequestDto;
import onepick.kanban.user.dto.PasswordRequestDto;
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
    private final SlackNotifier slackNotifier;

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

    @Transactional
    public void updatePassword(Long userId, PasswordRequestDto passwordRequestDto) {
        User user = userRepository.findByIdOrElseThrow(userId);
        boolean matches = passwordEncoder.matches(passwordRequestDto.getOldPassword(), user.getPassword());

        if (!matches) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        if (passwordRequestDto.getOldPassword().equals(passwordRequestDto.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 비밀번호를 사용할 수 없습니다.");
        }

        String encodedPassword = passwordEncoder.encode(passwordRequestDto.getNewPassword());
        user.updatePassword(encodedPassword);
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

        String message = user.get().getName() + "님이 로그인 하였습니다.";
        slackNotifier.sendNotification(message);

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    public JwtAuthResponse checkPassword(Long userId, String password) {
        User user = userRepository.findByIdOrElseThrow(userId);
        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (!matches) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다.");
        }

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String tempToken = this.jwtProvider.tempToken(authentication);

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), tempToken);
    }

    public boolean checkHeader(String extractToken) {
        if (!jwtProvider.isTempToken(extractToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "임시 토큰이 유효하지 않습니다.");
        } else {
            return true;
        }
    }

    public String extractToken(String tempToken) {
        if (tempToken != null && tempToken.startsWith("Bearer ")) {
            return tempToken.substring(7);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization 헤더에 Bearer 토큰이 포함되어야 합니다.");
    }
}