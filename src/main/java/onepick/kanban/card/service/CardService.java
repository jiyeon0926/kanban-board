package onepick.kanban.card.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.card.dto.CardRequestDto;
import onepick.kanban.card.dto.CardResponseDto;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.repository.CardAttachmentRepository;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.common.SlackNotifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardAttachmentRepository cardAttachmentRepository;
    private final BoardListRepository boardListRepository;
    private final SlackNotifier slackNotifier;

    public CardResponseDto createCard(Long boardListId, CardRequestDto requestDto) {

        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        Card card = new Card(boardList, null, requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
        card = cardRepository.save(card);

        String message = boardList.getTitle() + " 리스트의 " + card.getTitle() + " 카드가 생성되었습니다.";
        slackNotifier.sendNotification(message);

        return new CardResponseDto(card.getId(), card.getTitle(), card.getContents(), card.getDeadline());
    }

    public List<CardResponseDto> getCards(Long boardListId) {
        List<Card> cards = cardRepository.findByBoardListId(boardListId);

        return cards.stream()
                .map(card -> new CardResponseDto(card.getId(), card.getTitle(), card.getContents(), card.getDeadline()))
                .collect(Collectors.toList());
    }

    public CardResponseDto updateCard(Long cardId, CardRequestDto requestDto) {
        Card card = cardRepository.findByCardId(cardId)
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다."));

        card.updateCard(requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
        Card savedCard = cardRepository.save(card);

        return new CardResponseDto(savedCard.getId(), savedCard.getTitle(), savedCard.getContents(), savedCard.getDeadline());
    }
}
