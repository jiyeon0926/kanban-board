package onepick.kanban.board.repository;

import onepick.kanban.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByWorkspaceId(Long workspaceId);

    @Query("SELECT b FROM Board b WHERE b.id = :boardId")
    Optional<Board> findByBoardId(@Param("boardId") Long boardId);
}
