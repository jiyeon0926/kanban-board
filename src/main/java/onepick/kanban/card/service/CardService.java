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
import onepick.kanban.card.entity.CardAttachment;
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
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
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

        // 첨부 파일 URL 처리
        if (requestDto.getAttachmentUrls() != null && !requestDto.getAttachmentUrls().isEmpty()) {
            List<CardAttachment> attachments = requestDto.getAttachmentUrls().stream()
                    .map(url -> {
                        String imageName = extractFileNameFromUrl(url);
                        String fileType = determineFileType(imageName);
                        return new CardAttachment(card, url, imageName, fileType);
                    })
                    .collect(Collectors.toList());
            cardAttachmentRepository.saveAll(attachments);
            card.getCardAttachments().addAll(attachments);
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

        cardHistoryService.save(new CardHistory(savedCard, "카드가 수정되었습니다."));

        return mapToCardResponseDto(savedCard);
    }

    @Transactional
    public void deleteCard(Long cardId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다."));
        cardRepository.delete(card);
    }

    // S3 URL에서 파일 이름을 추출
    private String extractFileNameFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path =uri.getPath();

            return Paths.get(path).getFileName().toString();
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("유효하지 않은 URL입니다.");
        }
    }

    // 파일 이름에서 확장자 추출
    private String determineFileType(String fileName) {
        String extension = StringUtils.getFilenameExtension(fileName);

        return extension != null ? extension.toLowerCase() : "unknown";
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
