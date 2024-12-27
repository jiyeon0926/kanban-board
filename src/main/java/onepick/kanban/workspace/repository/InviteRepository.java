package onepick.kanban.workspace.repository;

import onepick.kanban.workspace.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Long> {
}
