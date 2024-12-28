package onepick.kanban.card.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.card.dto.CardRequestDto;
import onepick.kanban.card.dto.CardResponseDto;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.card.service.CardService;
import onepick.kanban.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/boards/{boardId}/lists/{listId}/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CardRepository cardRepository;

    // 카드 생성
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long boardId,
                                                      @PathVariable Long listId,
                                                      @Valid @RequestBody CardRequestDto requestDto) {
        CardResponseDto card = cardService.createCard(boardId, listId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    // 카드 상세 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> getCard(@PathVariable Long boardId,
                                                   @PathVariable Long boardListId,
                                                   @PathVariable Long cardId,
                                                   @AuthenticationPrincipal User user) {
        CardResponseDto card = cardService.getCard(cardId, user);

        return ResponseEntity.ok(card);
    }

    // 카드 수정
    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long cardId,
                                                      @RequestBody CardRequestDto requestDto,
                                                      @AuthenticationPrincipal User user) {
        CardResponseDto card = cardService.updateCard(cardId, requestDto, user);

        return ResponseEntity.ok(card);
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId,
                                           @AuthenticationPrincipal User user) {
        cardService.deleteCard(cardId, user);

        return ResponseEntity.noContent().build();
    }
}