package onepick.kanban.workspace.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.service.InviteService;
import onepick.kanban.workspace.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class UserWorkspaceController {

    private final WorkspaceService workspaceService;
    private final InviteService inviteService;

    // 워크스페이스 조회
    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> getAllWorkSpaces() {
        List<WorkspaceResponseDto> responseDtos = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok(responseDtos);
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
}