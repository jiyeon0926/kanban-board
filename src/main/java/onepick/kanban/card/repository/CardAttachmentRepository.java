package onepick.kanban.card.repository;

import onepick.kanban.card.entity.CardAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardAttachmentRepository extends JpaRepository<CardAttachment, Long> {
}
