package onepick.kanban.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.user.dto.LoginRequestDto;
import onepick.kanban.user.dto.UserRequestDto;
import onepick.kanban.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        userService.signup(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 회원탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("회원탈퇴가 완료되었습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃을 하였습니다.");
    }
}
