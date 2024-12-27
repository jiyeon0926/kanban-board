package onepick.kanban.workspace.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staffs/workspaces")
@RequiredArgsConstructor
public class StaffWorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크스페이스 생성
    @PostMapping
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@PathVariable Long creatorId, @RequestBody WorkspaceRequestDto requestDto) {
        WorkspaceResponseDto responseDto = workspaceService.createWorkspace(creatorId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 워크스페이스 수정
    @PutMapping("/{workspaceId}")
    public ResponseEntity<String> updateWorkspace(@PathVariable Long workspaceId, @RequestBody WorkspaceRequestDto requestDto) {
        workspaceService.updateWorkspace(workspaceId, requestDto);
        return ResponseEntity.ok("워크스페이스가 수정되었습니다.");
    }

    // 워크스페이스 삭제
    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<String> deleteWorkspace(@PathVariable Long workspaceId, Long deleterId) {
        workspaceService.deleteWorkspace(workspaceId, deleterId);
        return ResponseEntity.noContent().build();
    }

}
