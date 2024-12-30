package onepick.kanban.workspace.repository;

import onepick.kanban.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("SELECT w FROM Workspace w INNER JOIN FETCH w.members m WHERE w.id = :workspaceId")
    Workspace findMemberByWorkspaceId(@Param("workspaceId") Long workspaceId);

    @Query("SELECT w FROM Workspace w INNER JOIN Member m ON m.workspace.id = w.id INNER JOIN User u ON u.id = m.user.id WHERE u.id = :userId")
    List<Workspace> findWorkspacesByUserId(@Param("userId") Long userId);
}
