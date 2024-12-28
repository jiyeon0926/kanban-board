package onepick.kanban.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkspaceResponseDto {

    private Long workspaceId;
    private String title;
    private String contents;
}
