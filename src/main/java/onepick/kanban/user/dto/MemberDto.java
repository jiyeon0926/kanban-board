package onepick.kanban.user.dto;

import lombok.Getter;

@Getter
public class MemberDto {

    private Long memberId;
    private String role;

    public MemberDto(Long memberId, String role) {
        this.memberId = memberId;
        this.role = role;
    }
}
