package onepick.kanban.user.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.user.dto.UserRequestDto;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

    public void login(String email, String password) {
        // 사용자 정보가 올바른지 확인
            // 1. DB find 메소드 정의해서 확인
            // 2. authenticationManager.authenticate 활용
        // 올바르면 로그인 처리
        // 토큰 생성
        // 토큰 리턴
    }
}
