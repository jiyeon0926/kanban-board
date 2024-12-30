package onepick.kanban.user.dto;

import lombok.Getter;
import onepick.kanban.workspace.entity.Workspace;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberResponseDto {

    private Long workspaceId;
    private List<MemberDto> members;

    public MemberResponseDto(Long workspaceId, List<MemberDto> members) {
        this.workspaceId = workspaceId;
        this.members = members;
    }

    public static MemberResponseDto toDto(Workspace workspace) {
        List<MemberDto> members = workspace.getMembers().stream()
                .map(member -> new MemberDto(member.getId(), member.getRole().getName()))
                .collect(Collectors.toList());

        return new MemberResponseDto(
                workspace.getId(),
                members
        );
    }
}
