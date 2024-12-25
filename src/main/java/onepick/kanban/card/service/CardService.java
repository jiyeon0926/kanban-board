package onepick.kanban.card.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.card.dto.CardRequestDto;
import onepick.kanban.card.dto.CardResponseDto;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.repository.CardAttachmentRepository;
import onepick.kanban.card.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardAttachmentRepository cardAttachmentRepository;
    private final BoardListRepository boardListRepository;

    public CardResponseDto createCard(Long boardListId, CardRequestDto requestDto) {

        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        Card card = new Card(boardList, null, requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
        card = cardRepository.save(card);

        return new CardResponseDto(card.getId(), card.getTitle(), card.getContents(), card.getDeadline(), null);
    }

}
