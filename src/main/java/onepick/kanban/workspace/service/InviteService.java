package onepick.kanban.workspace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.workspace.dto.InviteRequestDto;
import onepick.kanban.workspace.entity.Invite;
import onepick.kanban.workspace.entity.Status;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.repository.InviteRepository;
import onepick.kanban.workspace.repository.WorkspaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 목록이 비어있습니다.");
        }

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 워크스페이스 ID입니다."));

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 ID입니다."));

        if (!invite.getWorkspace().getId().equals(workspaceId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 ID입니다.");
        }

        if (!isInvitee(invite.getInvitee())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "초대 받은 사용자가 아닙니다.");
        }

        // 수락 상태 -> 수락 || 거절 = "이미 수락된 상태입니다."
        if (invite.getStatus() == Status.ACCEPTED) {
            if (status.equals(Status.ACCEPTED.getName()) || status.equals(Status.REJECTED.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 수락된 상태입니다.");
            }
            // 거절 상태 -> 수락 상태 변경시: "관리자에게 재요청 문의해주세요."
        } else if (invite.getStatus() == Status.REJECTED) {
            if (status.equals(Status.ACCEPTED.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "관리자에게 재요청 문의해주세요.");
            } else {
                // 거절 상태 -> 거절 상태 변경시: "이미 거절된 상태입니다."
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 거절된 상태입니다.");
            }
        }

        invite.changeStatus(status);
        inviteRepository.save(invite);
    }

    // 관리자가 초대 취소
    @Transactional
    public void deleteInvite(Long workspaceId, Long inviteId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 ID입니다."));

        if (!invite.getWorkspace().getId().equals(workspaceId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 워크스페이스 ID입니다.");
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