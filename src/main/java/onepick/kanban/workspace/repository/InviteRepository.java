package onepick.kanban.workspace.repository;

import onepick.kanban.workspace.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    @Query("SELECT i FROM Invite i WHERE i.workspace.id = :workspaceId AND i.invitee.id IN :userIds")
    List<Invite> findByWorkspaceIdAndInviteeId(@Param("workspaceId") Long workspaceId, @Param("userIds") List<Long> userIds);
}
