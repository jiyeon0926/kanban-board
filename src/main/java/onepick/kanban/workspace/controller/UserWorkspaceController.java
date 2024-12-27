package onepick.kanban.workspace.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class UserWorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> getAllWorkSpaces() {
        List<WorkspaceResponseDto> responseDtos = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok(responseDtos);
    }
}