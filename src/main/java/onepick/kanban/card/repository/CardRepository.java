package onepick.kanban.card.repository;

import onepick.kanban.card.dto.CardSearchResponseDto;
import onepick.kanban.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByBoardListId(Long boardListId);

    @Query("SELECT c FROM Card c WHERE c.id = :cardId")
    Optional<Card> findByCardId(@Param("cardId") Long cardId);

    @Query("SELECT new onepick.kanban.card.dto.CardSearchResponseDto(c.id, c.title, c.contents, c.deadline) " +
            "FROM Card c " +
            "JOIN c.boardList l " +
            "JOIN l.board b " +
            "JOIN b.workspace w " +
            "JOIN c.assignees a " +
            "WHERE w.id = :workspaceId " +
            "AND b.id = :boardId " +
            "AND (:title IS NULL OR c.title LIKE %:title%)" +
            "AND (:contents IS NULL OR c.contents LIKE %:contents%)" +
            "AND (:startDate IS NULL OR c.deadline >= :startDate) " +
            "AND (:endDate IS NULL OR c.deadline <= :endDate) " +
            "AND (:assigneeName IS NULL OR a.name LIKE %:assigneeName%)"
    )
    Page<CardSearchResponseDto> findAllByBoard(@Param("workspaceId") Long workspaceId,
                                               @Param("boardId") Long boardId,
                                               @Param("title") String title,
                                               @Param("contents") String contents,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               @Param("assigneeName") String assigneeName,
                                               Pageable pageable);
}
