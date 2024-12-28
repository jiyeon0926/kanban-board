package onepick.kanban.workspace.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class InviteRequestDto {

    @NotEmpty(message = "초대할 이메일 리스트를 입력해야 합니다.")
    private List<String> emails;
}