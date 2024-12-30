package onepick.kanban.card.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import onepick.kanban.card.dto.CardRequestDto;
import onepick.kanban.card.dto.CardResponseDto;
import onepick.kanban.card.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/boards/{boardId}/lists/{listId}/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // 카드 생성
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long boardId,
                                                      @PathVariable Long listId,
                                                      @RequestParam("title") @NotBlank(message = "제목을 입력해주세요.") String title,
                                                      @RequestParam("contents") @NotBlank(message = "내용을 입력해주세요.") String contents,
                                                      @RequestParam(value = "deadline", required = false) LocalDateTime deadline,
                                                      @RequestParam(value = "assigneeIds", required = false) List<Long> assigneeIds,
                                                      @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments) {
        // CardRequestDto 생성
        CardRequestDto requestDto = new CardRequestDto(title, contents, deadline, assigneeIds, attachments);

        // 카드 생성 및 첨부파일 처리
        CardResponseDto card = cardService.createCard(boardId, listId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    // 카드 상세 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> getCard(@PathVariable Long boardId,
                                                   @PathVariable(name = "listId") Long boardListId,
                                                   @PathVariable Long cardId) {
        CardResponseDto card = cardService.getCard(cardId);

        return ResponseEntity.ok(card);
    }

    // 카드 수정
    @PutMapping(value = "/{cardId}", consumes = "multipart/form-data")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long cardId,
                                                      @RequestParam("title") @NotBlank(message = "제목을 입력해주세요.") String title,
                                                      @RequestParam("contents") @NotBlank(message = "내용을 입력해주세요.") String contents,
                                                      @RequestParam(value = "deadline", required = false) LocalDateTime deadline,
                                                      @RequestParam(value = "assigneeIds", required = false) List<Long> assigneeIds,
                                                      @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments) {
        // CardRequestDto 생성
        CardRequestDto requestDto = new CardRequestDto(title, contents, deadline, assigneeIds, attachments);

        // 카드 수정 및 첨부파일 처리
        CardResponseDto card = cardService.updateCard(cardId, requestDto);

        return ResponseEntity.ok(card);
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);

        return ResponseEntity.noContent().build();
    }
}