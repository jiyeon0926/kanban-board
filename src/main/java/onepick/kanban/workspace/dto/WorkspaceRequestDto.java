package onepick.kanban.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WorkspaceRequestDto {

    @NotBlank(message = "워크스페이스 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "워크스페이스 설명은 필수입니다.")
    private String contents;
}