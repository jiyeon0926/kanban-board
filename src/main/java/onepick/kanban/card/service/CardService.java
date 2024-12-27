package onepick.kanban.card.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.card.dto.CardRequestDto;
import onepick.kanban.card.dto.CardResponseDto;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.entity.CardHistory;
import onepick.kanban.card.repository.CardAttachmentRepository;
import onepick.kanban.card.repository.CardHistoryRepository;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardAttachmentRepository cardAttachmentRepository;
    private final BoardListRepository boardListRepository;
    private final CardHistoryRepository cardHistoryRepository;
    private final SlackNotifier slackNotifier;
    private final CardHistoryService cardHistoryService;

    @Transactional
    public CardResponseDto createCard(Long boardListId, CardRequestDto requestDto) {
//        validateUserRole(creator, Role.READONLY);

        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 항목입니다.");
        }

        Card card = new Card(boardList, null ,requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
        cardRepository.save(card);

        String message = boardList.getTitle() + " 리스트의 " + card.getTitle() + " 카드가 생성되었습니다.";
        slackNotifier.sendNotification(message);

        cardHistoryService.save(new CardHistory(card, "카드가 생성되었습니다."));

        return new CardResponseDto(card.getId(), card.getTitle(), card.getContents(), card.getDeadline(), null);
    }

    @Transactional(readOnly = true)
    public List<CardResponseDto> getCards(Long boardListId) {
//        validateUserWorkspaceAccess(boardListId);

        List<Card> cards = cardRepository.findByBoardListId(boardListId);
        return cards.stream()
                .map(card -> new CardResponseDto(card.getId(), card.getTitle(), card.getContents(), card.getDeadline(), null))
                .collect(Collectors.toList());
    }

    @Transactional
    public CardResponseDto updateCard(Long cardId, CardRequestDto requestDto) {
//        validateUserRole(creator, Role.READONLY);

        Card card = cardRepository.findByCardId(cardId)
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다."));

        card.updateCard(requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
        Card savedCard = cardRepository.save(card);

        cardHistoryService.save(new CardHistory(savedCard, "카드가 수정되었습니다."));

        return new CardResponseDto(savedCard.getId(), savedCard.getTitle(), savedCard.getContents(), savedCard.getDeadline(), null);
    }

    @Transactional
    public void deleteCard(Long cardId) {
//        validateUserRole(user, Role.READONLY);

        Card card = cardRepository.findByCardId(cardId)
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다."));
        cardRepository.delete(card);
    }

//    private void validateUserRole(User user, Role forbiddenRole) {
//        if (user.getRole().equals(forbiddenRole)) {
//            throw new SecurityException("권한이 없습니다.");
//        }
//    }
//
//    private void validateUserRole(User user, Role forbiddenRole) {
//        if (user.getRole().equals(forbiddenRole)) {
//            throw new SecurityException("권한이 없습니다.");
//        }
//    }
//
//    private void validateUserWorkspaceAccess(User user, Long boardListId) {
//        BoardList boardList = boardListRepository.findById(boardListId)
//                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));
//
//        if (!boardList.getBoard().getWorkspace().getInvites().stream().anyMatch(invite -> invite.getInvitee().equals(user))) {
//            throw new SecurityException("워크스페이스에 접근할 권한이 없습니다.");
//        }
//    }
}
