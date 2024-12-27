package onepick.kanban.workspace.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.exception.CustomException;
import onepick.kanban.exception.ErrorCode;
import onepick.kanban.user.entity.Role;
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

    // 초대 상태 수정
    public void updateInviteStatus(Long workspaceId, Long inviteeId, Long userId, String status) {
        Invite invite = inviteRepository.findById(inviteeId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INVITEE_ID));


        if (!invite.getWorkspace().getId().equals(workspaceId)) {
            throw new CustomException(ErrorCode.INVALID_WORKSPACE_ID, "워크스페이스 ID가 일치하지 않습니다.");
        }

        if (!isValidStatus(status)) {
            throw new CustomException(ErrorCode.INVALID_INVITE_STATUS, "유효하지 않은 초대 상태 값입니다." + status);
        }

        if (!canUserUpdateStatus(invite, userId, status)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_INVITE);
        }

        invite.changeStatus(Status.valueOf(status));
        inviteRepository.save(invite);
    }

    private boolean isValidStatus(String status) {
        try{
            Status.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean canUserUpdateStatus(Invite invite, Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Role userRole = user.getRole();
        Status targetStatus = Status.valueOf(status);

        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.STAFF)) {
            return targetStatus.equals(Status.CANCELLED);
        }

        if (user.getRole().equals(Role.USER)) {
            return targetStatus.equals(Status.ACCEPTED) || targetStatus.equals(Status.REJECTED);
        }
        return false;
    }
}
