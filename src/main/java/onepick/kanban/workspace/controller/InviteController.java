package onepick.kanban.workspace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.dto.InviteRequestDto;
import onepick.kanban.workspace.dto.InviteResponseDto;
import onepick.kanban.workspace.service.InviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/staffs/workspaces")
public class InviteController {

    private final InviteService inviteService;

    @PostMapping("/{workspaceId}/invite")
    public ResponseEntity<InviteResponseDto> inviteMembers(
            @PathVariable Long workspaceId,
            @PathVariable Long inviterId,
            @RequestBody @Valid InviteRequestDto requestDto) {
        InviteResponseDto response = inviteService.inviteMembers(workspaceId, inviterId, requestDto);
        return ResponseEntity.status(201).body(response);
    }


    @PatchMapping("/{workspaceId}/invites/{inviteId}")
    public ResponseEntity<InviteResponseDto> updateInviteStatus(
            @PathVariable Long workspaceId,
            @PathVariable Long inviteId,
            @PathVariable Long userId,
            @RequestBody @Valid String status
    ) {
        InviteResponseDto response = inviteService.updateInviteStatus(workspaceId, inviteId, userId, status);
        return ResponseEntity.ok(response);
    }
}
