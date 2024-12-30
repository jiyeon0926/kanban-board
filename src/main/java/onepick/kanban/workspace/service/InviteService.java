package onepick.kanban.workspace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.entity.Member;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.user.service.MemberService;
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
    private final MemberService memberService;
    private final SlackNotifier slackNotifier;

    // admin 관리자가 멤버 초대
    @Transactional
    public void inviteMembersByAdmin(Long workspaceId, List<String> emails) {
        Workspace workspace = getWorkspace(workspaceId, emails);

        // 관리자가 초대할 경우, 토큰에 저장된 이메일 가져와서 관리자 찾기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authEmail = auth.getName();
        Optional<User> inviter = userRepository.findByEmail(authEmail);

        // 요청한 이메일을 기반으로 멤버 찾기
        List<User> invitees = userRepository.findAllByEmailIn(emails);

        List<Invite> invites = invitees.stream()
                .map(invitee -> new Invite(workspace, inviter.get(), invitee))
                .collect(Collectors.toList());

        inviteRepository.saveAll(invites);

        String message = inviter.get().getName() + " 관리자가 " + workspace.getTitle() + " 워크스페이스 멤버 초대를 하였습니다.";
        slackNotifier.sendNotification(message);
    }

    // admin 관리자가 아닌 member 중 워크스페이스 관리자로 지정받은 사람이 멤버 초대
    @Transactional
    public void inviteMembersByStaff(Long workspaceId, List<String> emails) {
        Workspace workspace = getWorkspace(workspaceId, emails);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authEmail = auth.getName();
        Optional<User> inviter = userRepository.findByEmail(authEmail);

        Optional<Member> member = memberService.findByWorkspaceIdAndUserId(workspaceId, inviter.get().getId());

        if (member.get().getRole().equals(Role.STAFF)) {
            List<User> invitees = userRepository.findAllByEmailIn(emails);

            List<Invite> invites = invitees.stream()
                    .map(invitee -> new Invite(workspace, inviter.get(), invitee))
                    .collect(Collectors.toList());

            inviteRepository.saveAll(invites);

            String message = inviter.get().getName() + " 관리자가 " + workspace.getTitle() + " 워크스페이스 멤버 초대를 하였습니다.";
            slackNotifier.sendNotification(message);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "초대 권한이 없습니다.");
        }
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
        if (invite.getStatus().equals(Status.ACCEPTED)) {
            if (status.equals(Status.ACCEPTED.getName()) || status.equals(Status.REJECTED.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 수락된 상태입니다.");
            }
            // 거절 상태 -> 수락 상태 변경시: "관리자에게 재요청 문의해주세요."
        } else if (invite.getStatus().equals(Status.REJECTED)) {
            if (status.equals(Status.ACCEPTED.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "관리자에게 재요청 문의해주세요.");
            } else {
                // 거절 상태 -> 거절 상태 변경시: "이미 거절된 상태입니다."
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 거절된 상태입니다.");
            }
        }

        invite.changeStatus(status);
        inviteRepository.save(invite);

        if (invite.getStatus().equals(Status.ACCEPTED)) {
            memberService.save(new Member(invite.getWorkspace(), invite.getInvitee()));
        }
    }

    // STAFF 관리자가 초대 취소
    @Transactional
    public void deleteInviteByStaff(Long workspaceId, Long inviteId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 ID입니다."));

        if (!invite.getWorkspace().getId().equals(workspaceId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 워크스페이스 ID입니다.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authEmail = auth.getName();
        Optional<User> inviter = userRepository.findByEmail(authEmail);

        Optional<Member> member = memberService.findByWorkspaceIdAndUserId(workspaceId, inviter.get().getId());

        if (member.get().getRole().equals(Role.STAFF)) {
            inviteRepository.delete(invite);
        }
    }

    // 관리자가 초대 취소
    public void deleteInviteByAdmin(Long workspaceId, Long inviteId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 ID입니다."));

        if (!invite.getWorkspace().getId().equals(workspaceId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 워크스페이스 ID입니다.");
        }

        inviteRepository.delete(invite);
    }

    private Workspace getWorkspace(Long workspaceId, List<String> emails) {
        if (emails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 목록이 비어있습니다.");
        }

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 워크스페이스 ID입니다."));
        return workspace;
    }

    public boolean isInvitee(User invitee) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authEmail = auth.getName();
        Optional<User> user = userRepository.findByEmail(authEmail);

        return invitee.getEmail().equals(user.get().getEmail());
    }
}