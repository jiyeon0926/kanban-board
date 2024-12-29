package onepick.kanban.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.user.dto.MemberRoleRequestDto;
import onepick.kanban.user.dto.MemberRoleResponseDto;
import onepick.kanban.user.dto.MemberResponseDto;
import onepick.kanban.user.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자가 권한 지정
    @PutMapping("/workspaces/{workspaceId}/members/{memberId}/role")
    public ResponseEntity<MemberRoleResponseDto> updateRole(@PathVariable Long workspaceId,
                                                            @PathVariable Long memberId,
                                                            @Valid @RequestBody MemberRoleRequestDto memberRoleRequestDto) {
        return ResponseEntity.ok().body(adminService.updateRole(workspaceId, memberId, memberRoleRequestDto.getRole()));
    }

    // 권한 포함 워크스페이스별 멤버 다건 조회
    @GetMapping("/workspaces/{workspaceId}/members")
    public ResponseEntity<MemberResponseDto> findMemberByWorkspaceId(@PathVariable Long workspaceId) {
        return ResponseEntity.ok().body(adminService.findMemberByWorkspaceId(workspaceId));
    }
}
