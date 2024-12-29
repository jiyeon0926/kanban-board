package onepick.kanban.workspace.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.dto.InviteRequestDto;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.service.InviteService;
import onepick.kanban.workspace.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final InviteService inviteService;

    // 워크스페이스 수정
    @PutMapping("/{workspaceId}")
    public ResponseEntity<String> updateWorkspace(@PathVariable Long workspaceId, @Valid @RequestBody WorkspaceRequestDto requestDto) {
        workspaceService.updateWorkspace(workspaceId, requestDto);
        return ResponseEntity.ok("워크스페이스가 수정되었습니다.");
    }

    // 워크스페이스 삭제
    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
        return ResponseEntity.noContent().build();
    }

    // 워크스페이스 조회
    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> getAllWorkSpaces() {
        List<WorkspaceResponseDto> responseDtos = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok(responseDtos);
    }

    // 멤버 초대
    @PostMapping("/{workspaceId}/invite")
    public ResponseEntity<String> inviteMembersByStaff(
            @PathVariable Long workspaceId,
            @Valid @RequestBody InviteRequestDto requestDto) {
        inviteService.inviteMembersByStaff(workspaceId, requestDto.getEmails());
        return ResponseEntity.status(HttpStatus.CREATED).body("초대를 요청하였습니다.");
    }

    // 멤버 초대 상태 수정 (accepted, rejected)
    @PatchMapping("/{workspaceId}/invites/{inviteId}")
    public ResponseEntity<String> updateInviteStatus(
            @PathVariable Long workspaceId,
            @PathVariable Long inviteId,
            @Pattern(regexp = "^(accepted|rejected)$", message = "accepted 또는 rejected 상태만 허용합니다.") @RequestParam String status) {
        inviteService.updateInviteStatus(workspaceId, inviteId, status);
        return ResponseEntity.ok().body("초대 상태가 성공적으로 업데이트되었습니다.");
    }

    // 멤버 초대 취소
    @DeleteMapping("/{workspaceId}/invites/{inviteId}")
    public ResponseEntity<String> deleteInvite(
            @PathVariable Long workspaceId,
            @PathVariable Long inviteId) {
        inviteService.deleteInvite(workspaceId, inviteId);
        return ResponseEntity.ok().body("관리자가 초대 취소를 하였습니다.");
    }
}