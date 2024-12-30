package onepick.kanban.card.repository;

import onepick.kanban.card.entity.CardAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardAttachmentRepository extends JpaRepository<CardAttachment, Long> {
    List<CardAttachment> findAllByCardId(Long cardId);
}
