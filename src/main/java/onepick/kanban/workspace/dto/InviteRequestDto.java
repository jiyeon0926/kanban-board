package onepick.kanban.workspace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import onepick.kanban.user.entity.User;

import java.util.List;

@Getter
public class InviteRequestDto {

    @NotEmpty(message = "초대할 이메일 리스트를 입력해야 합니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private List<String> emails;
}
