package onepick.kanban.board.repository;

import onepick.kanban.board.entity.Board;
import onepick.kanban.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByWorkspaceId(Long workspaceId);

//    @Query("SELECT b FROM Board b WHERE b.id = :boardId")
//    Optional<Board> findByBoardId(@Param("boardId") Long boardId);

//    default Board findByBoardId(Long boardId) {
//        return findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 데이터가 존재하지 않습니다."));
//    }

    @Query("SELECT b FROM Board b INNER JOIN BoardList l ON b.id = l.board.id INNER JOIN Card c ON l.id = c.boardList.id WHERE b.workspace.id = :workspaceId AND b.id = :boardId")
    Board findByBoardId(@Param("workspaceId") Long workspaceId, @Param("boardId") Long boardId);
}
