package onepick.kanban.card.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.card.dto.CardAttachmentDto;
import onepick.kanban.card.dto.CardHistoryDto;
import onepick.kanban.card.dto.CardRequestDto;
import onepick.kanban.card.dto.CardResponseDto;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.entity.CardHistory;
import onepick.kanban.card.repository.CardAttachmentRepository;
import onepick.kanban.card.repository.CardHistoryRepository;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.comment.dto.CommentResponseDto;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
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
    private final UserRepository userRepository; // 사용자 조회를 위한 레포지토리 추가
    private final SlackNotifier slackNotifier;
    private final CardHistoryService cardHistoryService;
    private final BoardRepository boardRepository;
    private final CardAttachmentService cardAttachmentService;

    @Transactional
    public CardResponseDto createCard(Long boardId, Long listId, CardRequestDto requestDto) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));

        BoardList boardList = boardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 항목입니다.");
        }

        // 담당자 조회
        List<User> assignees = userRepository.findAllById(requestDto.getAssigneeIds());
        if (assignees.size() != requestDto.getAssigneeIds().size()) {
            throw new IllegalArgumentException("존재하지 않는 담당자가 포함되어 있습니다.");
        }

        Card card = new Card(boardList, assignees, requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline());
        cardRepository.save(card);

        // 첨부 파일 처리: 실제 파일을 CardAttachmentService로 전달
        if (requestDto.getAttachments() != null && !requestDto.getAttachments().isEmpty()) {
            List<CardAttachmentDto> attachmentDtos = cardAttachmentService.createAttachments(card.getId(), requestDto.getAttachments());
            // 필요 시 CardResponseDto에 첨부파일 추가 (이미 CardResponseDto에 포함되어 있음)
        }

        String message = boardList.getTitle() + " 리스트의 " + card.getTitle() + " 카드가 생성되었습니다.";
        slackNotifier.sendNotification(message);

        cardHistoryService.save(new CardHistory(card, "카드가 생성되었습니다."));

        return mapToCardResponseDto(card);
    }

    @Transactional(readOnly = true)
    public List<CardResponseDto> getCards(Long boardListId) {

        List<Card> cards = cardRepository.findByBoardListId(boardListId);

        return cards.stream()
                .map(this::mapToCardResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다."));

        return mapToCardResponseDto(card);
    }

    @Transactional
    public CardResponseDto updateCard(Long cardId, CardRequestDto requestDto) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다."));

        List<User> assignees = userRepository.findAllById(requestDto.getAssigneeIds());
        if (assignees.size() != requestDto.getAssigneeIds().size()) {
            throw new IllegalArgumentException("존재하지 않는 담당자가 포함되어 있습니다.");
        }

        card.updateCard(requestDto.getTitle(), requestDto.getContents(), requestDto.getDeadline(), assignees);
        Card savedCard = cardRepository.save(card);

        // 첨부 파일 처리 (업데이트 시 새로운 파일 추가)
        if (requestDto.getAttachments() != null && !requestDto.getAttachments().isEmpty()) {
            List<CardAttachmentDto> attachmentDtos = cardAttachmentService.createAttachments(card.getId(), requestDto.getAttachments());
            // 필요 시 기존 첨부파일과 병합
        }

        cardHistoryService.save(new CardHistory(savedCard, "카드가 수정되었습니다."));

        return mapToCardResponseDto(savedCard);
    }

    @Transactional
    public void deleteCard(Long cardId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다."));
        cardRepository.delete(card);
    }

    // 카드 엔티티를 응답 DTO로 Mapping
    private CardResponseDto mapToCardResponseDto(Card card) {

        List<CardAttachmentDto> attachments = card.getCardAttachments().stream()
                .map(att -> new CardAttachmentDto(att.getId(), att.getImage(), att.getImageName(), att.getFileType()))
                .collect(Collectors.toList());

        List<CommentResponseDto> comments = card.getComments().stream()
                .map(c -> new CommentResponseDto(c.getId(), c.getContents(), c.getEmoji(), c.getUser().getName(), card.getId(), c.getCreatedAt(), c.getModifiedAt()))
                .collect(Collectors.toList());

        List<CardHistoryDto> histories = card.getCardHistories().stream()
                .map(h -> new CardHistoryDto(h.getId(), h.getLogMessage(), h.getCreatedAt()))
                .collect(Collectors.toList());

        return new CardResponseDto(
                card.getId(),
                card.getTitle(),
                card.getContents(),
                card.getDeadline(),
                attachments,
                comments,
                histories
        );
    }
}
