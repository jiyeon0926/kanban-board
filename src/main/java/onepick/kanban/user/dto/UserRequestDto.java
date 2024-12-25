package onepick.kanban.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import onepick.kanban.user.entity.User;

@Getter
public class UserRequestDto {

    @Email(message = "이메일 형식이 아닙니다.")
    @Size(max = 100)
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "name은 필수 입력 항목 입니다.")
    @Size(max = 10)
    private String name;

    @NotBlank(message = "user 또는 admin 권한으로 입력해주세요.")
    @Size(max = 10)
    @Pattern(regexp = "^(user|admin)$", message = "user 또는 admin 권한만 허용합니다.")
    private String role;

    public UserRequestDto(String email, String password, String name, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public User toEntity() {
        return new User(
                this.email,
                this.password,
                this.name,
                this.role
        );
    }

    public void updatePassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
}