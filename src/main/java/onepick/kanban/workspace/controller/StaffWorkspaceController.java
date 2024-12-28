package onepick.kanban.workspace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.dto.InviteRequestDto;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.service.InviteService;
import onepick.kanban.workspace.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staffs/workspaces")
@RequiredArgsConstructor
public class StaffWorkspaceController {

    private final WorkspaceService workspaceService;
    private final InviteService inviteService;

    // 워크스페이스 생성
    @PostMapping
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@Valid @RequestBody WorkspaceRequestDto requestDto) {
        WorkspaceResponseDto responseDto = workspaceService.createWorkspace(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

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

    // 멤버 초대
    @PostMapping("/{workspaceId}/invite")
    public ResponseEntity<String> inviteMembers(
            @PathVariable Long workspaceId,
            @Valid @RequestBody InviteRequestDto requestDto) {
        inviteService.inviteMembers(workspaceId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("초대를 요청하였습니다.");
    }

    // 멤버 초대 취소
    @DeleteMapping("/{workspaceId}/invites/{inviteId}")
    public ResponseEntity<String> updateInviteStatus(
            @PathVariable Long workspaceId,
            @PathVariable Long inviteId) {
        inviteService.deleteInvite(workspaceId, inviteId);
        return ResponseEntity.ok().body("멤버 초대를 취소하였습니다.");
    }
}
