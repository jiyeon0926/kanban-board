package onepick.kanban.workspace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.dto.InviteRequestDto;
import onepick.kanban.workspace.service.InviteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/staffs/workspaces")
public class InviteController {

    private final InviteService inviteService;

    // 멤버 초대
    @PostMapping("/{workspaceId}/invite")
    public ResponseEntity<String> inviteMembers(
            @PathVariable Long workspaceId,
            @Valid @RequestBody InviteRequestDto requestDto) {
        inviteService.inviteMembers(workspaceId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("초대를 요청하였습니다.");
    }

    // 초대 상태 수정
    @PatchMapping("/{workspaceId}/invites/{inviteId}")
    public ResponseEntity<String> updateInviteStatus(
            @PathVariable Long workspaceId,
            @PathVariable Long inviteId,
            @PathVariable Long userId,
            @RequestBody @Valid String status) {
        inviteService.updateInviteStatus(workspaceId, inviteId, userId, status);
        return ResponseEntity.ok().body("초대 상태가 성공적으로 업데이트되었습니다.");
    }
}