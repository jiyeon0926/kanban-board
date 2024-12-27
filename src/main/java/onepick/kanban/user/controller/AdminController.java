package onepick.kanban.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.user.dto.RoleRequestDto;
import onepick.kanban.user.dto.RoleResponseDto;
import onepick.kanban.user.dto.UserResponseDto;
import onepick.kanban.user.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins/users")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자가 권한 지정
    @PutMapping("/{userId}/role")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable Long userId,
                                                      @Valid @RequestBody RoleRequestDto roleRequestDto) {
        return ResponseEntity.ok().body(adminService.updateRole(userId, roleRequestDto.getRole()));
    }

    // 사용자 다건 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok().body(adminService.findAll());
    }
}
