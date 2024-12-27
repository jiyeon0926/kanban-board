package onepick.kanban.workspace.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.exception.CustomException;
import onepick.kanban.exception.ErrorCode;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.workspace.dto.InviteRequestDto;
import onepick.kanban.workspace.dto.InviteResponseDto;
import onepick.kanban.workspace.entity.Invite;
import onepick.kanban.workspace.entity.Status;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.repository.InviteRepository;
import onepick.kanban.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    // 멤버 초대
    public InviteResponseDto inviteMembers(Long workspaceId, Long inviterId, @Valid InviteRequestDto requestDto) {

        if (requestDto.getEmails() == null || requestDto.getEmails().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "이메일 목록이 비어있습니다.");
        }

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_WORKSPACE_ID));

        User inviter = userRepository.findById(inviterId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_INVITER));

        if (!inviter.getRole().equals(Role.STAFF) && !inviter.getRole().equals(Role.ADMIN)) {
            throw new CustomException(ErrorCode.INVALID_WORKSPACE_INVITER);
        }

        List<User> invitees = userRepository.findAllByEmail(requestDto.getEmails());
        if (invitees.size() != requestDto.getEmails().size()) {
            List<String> notFoundEmails = requestDto.getEmails().stream()
                    .filter(email -> invitees.stream().noneMatch(user -> user.getEmail().equals(email)))
                    .toList();

            throw new CustomException(ErrorCode.USER_NOT_FOUND_INVITEE,
                    "다음 이메일이 존재하지 않습니다: " + String.join(", ", notFoundEmails));
        }

        invitees.forEach(invitee -> {
            Invite invite = new Invite(workspace, inviter, invitee);
            inviteRepository.save(invite);
        });

        return new InviteResponseDto("초대를 요청하였습니다.");
    }

    // 초대 상태 수정
    public InviteResponseDto updateInviteStatus(Long workspaceId, Long inviteeId, Long userId, @Valid String status) {
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

        return new InviteResponseDto("초대 상태가 성공적으로 업데이트되었습니다.");
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
