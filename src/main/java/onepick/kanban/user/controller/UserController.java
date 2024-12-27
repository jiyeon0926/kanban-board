package onepick.kanban.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.common.CommonResponseBody;
import onepick.kanban.user.dto.*;
import onepick.kanban.user.service.UserService;
import org.springframework.http.HttpHeaders;
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

    // 비밀번호 변경
    @PatchMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId,
                                                 @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        userService.updatePassword(userId, passwordRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body("비밀번호 변경이 완료되었습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponseBody<JwtAuthResponse>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        JwtAuthResponse authResponse = userService.login(loginRequestDto);

        return ResponseEntity.ok(new CommonResponseBody<>("로그인을 성공하였습니다.", authResponse));
    }

    // 탈퇴 전 비밀번호 확인
    @PostMapping("/{userId}/check")
    public ResponseEntity<CommonResponseBody<JwtAuthResponse>> checkPassword(@PathVariable Long userId,
                                                                             @Valid @RequestBody CheckRequestDto checkRequestDto) {
        JwtAuthResponse authResponse = userService.checkPassword(userId, checkRequestDto.getPassword());

        return ResponseEntity.ok(new CommonResponseBody<>("비밀번호를 확인하였습니다.", authResponse));
    }

    // 회원탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String tempToken) {
        String extractToken = userService.extractToken(tempToken);
        boolean checked = userService.checkHeader(extractToken);

        if (checked) {
            userService.deleteUser(userId);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("회원탈퇴가 완료되었습니다.");
    }
}
