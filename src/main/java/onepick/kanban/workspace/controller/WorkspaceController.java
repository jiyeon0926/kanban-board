package onepick.kanban.workspace.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.service.WorkspaceService;
import org.hibernate.jdbc.Work;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.Response;

import java.util.List;

@RestController
@RequestMapping("/staffs/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크스페이스 생성
    @PostMapping
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@RequestBody WorkspaceRequestDto requestDto) {
        WorkspaceResponseDto responseDto = workspaceService.createWorkspace(requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    // 워크스페이스 조회
    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> getWorkSpaces() {
        List<WorkspaceResponseDto> responseDtos = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok(responseDtos);
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
        return ResponseEntity.ok("워크스페이스가 삭제되었습니다.");
    }

}