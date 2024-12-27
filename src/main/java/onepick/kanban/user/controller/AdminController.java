package onepick.kanban.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.user.dto.RoleRequestDto;
import onepick.kanban.user.dto.RoleResponseDto;
import onepick.kanban.user.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자가 권한 지정
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable Long userId,
                                                      @Valid @RequestBody RoleRequestDto roleRequestDto) {
        return ResponseEntity.ok().body(adminService.updateRole(userId, roleRequestDto.getRole()));
    }
}
