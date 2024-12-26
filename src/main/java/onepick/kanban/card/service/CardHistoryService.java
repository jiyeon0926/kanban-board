package onepick.kanban.card.service;

import onepick.kanban.card.entity.CardHistory;
import onepick.kanban.card.repository.CardHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardHistoryService {
    private final CardHistoryRepository cardHistoryRepository;

    public CardHistoryService(CardHistoryRepository cardHistoryRepository) {
        this.cardHistoryRepository = cardHistoryRepository;
    }

    @Transactional
    public void save(CardHistory cardHistory) {
        cardHistoryRepository.save(cardHistory);

        if (cardHistory != null) {
            throw new RuntimeException();
        }
    }
}
