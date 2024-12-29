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
@RequestMapping("/admins/workspaces")
@RequiredArgsConstructor
public class AdminWorkspaceController {

    private final WorkspaceService workspaceService;
    private final InviteService inviteService;

    // 워크스페이스 생성
    @PostMapping
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@Valid @RequestBody WorkspaceRequestDto requestDto) {
        WorkspaceResponseDto responseDto = workspaceService.createWorkspace(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 멤버 초대
    @PostMapping("/{workspaceId}/invite")
    public ResponseEntity<String> inviteMembers(
            @PathVariable Long workspaceId,
            @Valid @RequestBody InviteRequestDto requestDto) {
        inviteService.inviteMembers(workspaceId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("초대를 요청하였습니다.");
    }
}
