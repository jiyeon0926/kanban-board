package onepick.kanban.card.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.dto.CardSearchResponseDto;
import onepick.kanban.card.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardSearchService {

    private final CardRepository cardRepository;

    public Page<CardSearchResponseDto> findAllByBoard(Long workspaceId,
                                                      Long boardId,
                                                      String title,
                                                      String contents,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate,
                                                      String assigneeName,
                                                      Pageable pageable) {
        return cardRepository.findAllByBoard(workspaceId, boardId, title, contents, startDate, endDate, assigneeName, pageable);
    }
}
