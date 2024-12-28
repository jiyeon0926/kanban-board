package onepick.kanban.card.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.entity.CardHistory;
import onepick.kanban.card.repository.CardHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardHistoryService {
    private final CardHistoryRepository cardHistoryRepository;

    @Transactional
    public void save(CardHistory cardHistory) {
        cardHistoryRepository.save(cardHistory);
    }
}
