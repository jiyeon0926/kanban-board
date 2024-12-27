package onepick.kanban.user.dto;

import lombok.Getter;
import onepick.kanban.user.entity.User;

import java.time.LocalDateTime;

@Getter
public class RoleResponseDto {

    private Long userId;
    private String name;
    private String role;
    private LocalDateTime modifiedAt;

    public RoleResponseDto(Long userId, String name, String role, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.modifiedAt = modifiedAt;
    }

    public static RoleResponseDto toDto(User user) {
        return new RoleResponseDto(
                user.getId(),
                user.getName(),
                user.getRole().getName(),
                user.getModifiedAt()
        );
    }
}
