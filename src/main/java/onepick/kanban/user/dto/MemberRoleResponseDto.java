package onepick.kanban.user.dto;

import lombok.Getter;
import onepick.kanban.user.entity.Member;
import java.time.LocalDateTime;

@Getter
public class MemberRoleResponseDto {

    private Long memberId;
    private String role;
    private LocalDateTime modifiedAt;

    public MemberRoleResponseDto(Long memberId, String role, LocalDateTime modifiedAt) {
        this.memberId = memberId;
        this.role = role;
        this.modifiedAt = modifiedAt;
    }

    public static MemberRoleResponseDto toDto(Member member) {
        return new MemberRoleResponseDto(
                member.getId(),
                member.getRole().getName(),
                member.getModifiedAt()
        );
    }
}
