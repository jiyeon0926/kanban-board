package onepick.kanban.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberRoleRequestDto {

    @NotBlank(message = "지정할 권한을 입력해주세요.")
    @Size(max = 10)
    @Pattern(regexp = "^(member|staff|readonly)$", message = "admin 권한으로 지정할 수 없습니다.")
    private String role;
}
