package onepick.kanban.card.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
}
