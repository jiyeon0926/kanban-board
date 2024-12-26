package onepick.kanban.card.repository;

import onepick.kanban.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByBoardListId(Long boardListId);

    @Query("SELECT c FROM Card c WHERE c.id = :cardId")
    Optional<Card> findByCardId(@Param("cardId") Long cardId);
}
