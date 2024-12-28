package onepick.kanban.workspace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onepick.kanban.exception.CustomException;
import onepick.kanban.exception.ErrorCode;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.workspace.dto.InviteRequestDto;
import onepick.kanban.workspace.entity.Invite;
import onepick.kanban.workspace.entity.Status;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.repository.InviteRepository;
import onepick.kanban.workspace.repository.WorkspaceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    // 멤버 초대
    @Transactional
    public void inviteMembers(Long workspaceId, InviteRequestDto requestDto) {
        if (requestDto.getEmails().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "이메일 목록이 비어있습니다.");
        }

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_WORKSPACE_ID));

        // 토큰에 저장된 이메일 가져와서 관리자 찾기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authEmail = auth.getName();
        Optional<User> inviter = userRepository.findByEmail(authEmail);

        // 요청한 이메일을 기반으로 멤버 찾기
        List<User> invitees = userRepository.findAllByEmailIn(requestDto.getEmails());

        List<Invite> invites = invitees.stream()
                .map(invitee -> new Invite(workspace, inviter.get(), invitee))
                .collect(Collectors.toList());

        inviteRepository.saveAll(invites);
    }

    // 초대 받은 사용자가 초대 상태 수정
    @Transactional
    public void updateInviteStatus(Long workspaceId, Long inviteId, String status) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INVITEE_ID));

        if (!invite.getWorkspace().getId().equals(workspaceId)) {
            throw new CustomException(ErrorCode.INVALID_WORKSPACE_ID);
        }

        if (!isInvitee(invite.getInvitee())) {
            throw new CustomException(ErrorCode.INVALID_WORKSPACE_INVITEE);
        }

        invite.changeStatus(status);
        inviteRepository.save(invite);
    }

    // 관리자가 초대 취소
    @Transactional
    public void deleteInvite(Long workspaceId, Long inviteId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INVITEE_ID));

        if (!invite.getWorkspace().getId().equals(workspaceId)) {
            throw new CustomException(ErrorCode.INVALID_WORKSPACE_ID);
        }

        inviteRepository.delete(invite);
    }

    public boolean isInvitee(User invitee) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authEmail = auth.getName();
        Optional<User> user = userRepository.findByEmail(authEmail);

        return invitee.getEmail().equals(user.get().getEmail());
    }
}
