package onepick.kanban.user.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.dto.MemberRoleResponseDto;
import onepick.kanban.user.dto.MemberResponseDto;
import onepick.kanban.user.entity.Member;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.repository.MemberRepository;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.entity.Invite;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.service.InviteService;
import onepick.kanban.workspace.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final WorkspaceService workspaceService;
    private final InviteService inviteService;
    private final SlackNotifier slackNotifier;

    @Transactional
    public WorkspaceResponseDto createWorkspace(WorkspaceRequestDto requestDto) {
        Workspace workspace = workspaceService.createWorkspace(requestDto.getTitle(), requestDto.getContents());

        String message = workspace.getTitle() + " 워크스페이스가 생성되었습니다.";
        slackNotifier.sendNotification(message);

        return new WorkspaceResponseDto(workspace.getId(), workspace.getTitle(), workspace.getContents());
    }

    @Transactional
    public void inviteMembersByAdmin(Long workspaceId, List<String> emails) {
        inviteService.inviteMembersByAdmin(workspaceId, emails);
    }

    @Transactional
    public MemberRoleResponseDto updateRole(Long workspaceId, Long memberId, String role) {
        Member member = memberRepository.findByWorkspaceIdAndMemberId(workspaceId, memberId);

        if (member.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "관리자의 권한을 수정할 수 없습니다.");
        }

        member.updateRole(role);
        memberRepository.save(member);

        if (member.getRole().equals(Role.STAFF)) {
            String message = member.getUser().getName() + "님은 워크스페이스 관리자로 지정됐습니다.";
            slackNotifier.sendNotification(message);
        }

        return MemberRoleResponseDto.toDto(member);
    }

    @Transactional
    public void deleteInviteByAdmin(Long workspaceId, Long inviteId) {
        inviteService.deleteInviteByAdmin(workspaceId, inviteId);
    }

    public MemberResponseDto findMemberByWorkspaceId(Long workspaceId) {
        Workspace workspace = workspaceService.findMemberByWorkspaceId(workspaceId);

        return MemberResponseDto.toDto(workspace);
    }
}
