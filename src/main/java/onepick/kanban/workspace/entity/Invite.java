package onepick.kanban.workspace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onepick.kanban.common.Timestamp;
import onepick.kanban.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
public class Invite extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "inviter_id")
    private User inviter; // 초대를 한 사람 (관리자)

    @ManyToOne
    @JoinColumn(name = "invitee_id")
    private User invitee; // 멤버 초대를 받은 사람 (일반 사용자)

    public Invite(Workspace workspace, User inviter, User invitee) {
        this.workspace = workspace;
        this.inviter = inviter;
        this.invitee = invitee;
    }

    public void changeStatus(Status newStatus) {
        this.status = newStatus;
    }
}