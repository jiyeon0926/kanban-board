package onepick.kanban.card.repository;

import onepick.kanban.card.entity.CardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardHistoryRepository extends JpaRepository<CardHistory, Long> {
}
