package onepick.kanban.user.dto;

import lombok.Getter;
import onepick.kanban.user.entity.User;

@Getter
public class UserResponseDto {

    private Long userId;
    private String email;
    private String name;
    private String role;

    public UserResponseDto(Long userId, String email, String name, String role) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().getName()
        );
    }
}
