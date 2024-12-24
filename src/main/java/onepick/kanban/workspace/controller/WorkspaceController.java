package onepick.kanban.workspace.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.workspace.service.WorkspaceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;
}
