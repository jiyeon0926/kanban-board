package onepick.kanban.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onepick.kanban.common.Timestamp;
import onepick.kanban.workspace.entity.Workspace;

@Entity
@Getter
@NoArgsConstructor
public class Member extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.MEMBER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Member(Workspace workspace, User user) {
        this.workspace = workspace;
        this.user = user;
    }

    public void updateRole(String role) {
        this.role = Role.of(role);
    }
}
