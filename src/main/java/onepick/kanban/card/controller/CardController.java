package onepick.kanban.card.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.dto.BoardResponseDto;
import onepick.kanban.board.service.BoardService;
import onepick.kanban.card.dto.CardRequestDto;
import onepick.kanban.card.dto.CardResponseDto;
import onepick.kanban.card.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boards/{boardId}/lists/{listId}/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // 카드 생성
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long boardListId, @RequestBody CardRequestDto requestDto) {
        CardResponseDto card = cardService.createCard(boardListId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    // 카드 상세 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<List<CardResponseDto>> getCards(@RequestParam Long boardListId) {
        List<CardResponseDto> cards = cardService.getCards(boardListId);

        return ResponseEntity.ok(cards);
    }
}