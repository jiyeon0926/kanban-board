package onepick.kanban.workspace.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public InviteResponseDto inviteMembers(Long workspaceId, Long inviterId, InviteRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 워크스페이스 ID입니다."));

        User inviter = userRepository.findById(inviterId)
                .orElseThrow(() -> new IllegalArgumentException("초대한 사용자를 찾을 수 없습니다."));

        if (!inviter.getRole().equals(Role.WORKSPACE_ADMIN) && !inviter.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("초대 권한이 없는 사용자입니다.");
        }

        if (requestDto.getEmails() == null || requestDto.getEmails().isEmpty()) {
            return new InviteResponseDto("초대할 이메일 리스트가 비어 있습니다.");
        }

        List<String> invalidEmails = new ArrayList<>();
        for (String email : requestDto.getEmails()) {
            userRepository.findByEmail(email).ifPresentOrElse(
                    invitee -> {
                        Invite invite = new Invite(workspace, inviter, invitee);
                        inviteRepository.save(invite);
                    },
                    () -> invalidEmails.add(email)
            );
        }

        if (!invalidEmails.isEmpty()) {
            return new InviteResponseDto("다음 이메일로 초대를 실패하였습니다: " + String.join(", ", invalidEmails));
        }

        return new InviteResponseDto("초대를 요청하였습니다.");
    }

    public InviteResponseDto updateInviteStatus(Long workspaceId, Long inviteId, Long userId, @Valid String status) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new IllegalArgumentException("초대 ID가 유효하지 않습니다."));


        if (!invite.getWorkspace().getId().equals(workspaceId)) {
            return new InviteResponseDto("초대 상태를 수정할 수 없습니다: 워크스페이스 ID가 유효하지 않습니다.");
        }

        if (!isValidStatus(status)) {
            return new InviteResponseDto("초대 상태를 수정할 수 없습니다: 상태 값이 유효하지 않습니다.");
        }

        if (!canUserUpdateStatus(invite, userId, status)) {
            return new InviteResponseDto("초대 상태를 수정할 수 없습니다: 권한이 없습니다.");
        }

        invite.changeStatus(Status.valueOf(status));
        inviteRepository.save(invite);

        return new InviteResponseDto("초대 상태가 성공적으로 업데이트되었습니다.");
    }

    private boolean isValidStatus(String status) {
        try {
            Status.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean canUserUpdateStatus(Invite invite, Long userId, String status) {
        if (invite.getInviter().getId().equals(userId) && status.equals("CANCELLED")) {
            return true;
        }
        if (invite.getInvitee().getId().equals(userId) && (status.equals("ACCEPTED") || status.equals("REJECTED"))) {
            return true;
        }
        return false;
    }
}