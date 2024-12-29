package onepick.kanban.workspace.repository;

import onepick.kanban.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("SELECT w FROM Workspace w INNER JOIN FETCH w.members m WHERE w.id = :workspaceId")
    Workspace findMemberByWorkspaceId(@Param("workspaceId") Long workspaceId);
}
