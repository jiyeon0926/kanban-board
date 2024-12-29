package onepick.kanban.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.user.dto.MemberRoleRequestDto;
import onepick.kanban.user.dto.MemberRoleResponseDto;
import onepick.kanban.user.dto.MemberResponseDto;
import onepick.kanban.user.service.AdminService;
import onepick.kanban.workspace.dto.InviteRequestDto;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 워크스페이스 생성
    @PostMapping("/workspaces")
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@Valid @RequestBody WorkspaceRequestDto requestDto) {
        WorkspaceResponseDto responseDto = adminService.createWorkspace(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 멤버 초대
    @PostMapping("/workspaces/{workspaceId}/invite")
    public ResponseEntity<String> inviteMembersByAdmin(@PathVariable Long workspaceId,
                                                       @Valid @RequestBody InviteRequestDto requestDto) {
        adminService.inviteMembersByAdmin(workspaceId, requestDto.getEmails());
        return ResponseEntity.status(HttpStatus.CREATED).body("관리자가 초대를 요청하였습니다.");
    }

    // admin 관리자가 member의 권한을 지정
    @PutMapping("/workspaces/{workspaceId}/members/{memberId}/role")
    public ResponseEntity<MemberRoleResponseDto> updateRole(@PathVariable Long workspaceId,
                                                            @PathVariable Long memberId,
                                                            @Valid @RequestBody MemberRoleRequestDto memberRoleRequestDto) {
        return ResponseEntity.ok().body(adminService.updateRole(workspaceId, memberId, memberRoleRequestDto.getRole()));
    }

    // 워크스페이스의 멤버 전체 조회
    @GetMapping("/workspaces/{workspaceId}/members")
    public ResponseEntity<MemberResponseDto> findMemberByWorkspaceId(@PathVariable Long workspaceId) {
        return ResponseEntity.ok().body(adminService.findMemberByWorkspaceId(workspaceId));
    }
}
