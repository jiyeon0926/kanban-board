package onepick.kanban.card.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.dto.CardSearchResponseDto;
import onepick.kanban.card.service.CardSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CardSearchController {

    private final CardSearchService cardSearchService;

    @GetMapping("/workspaces/{workspaceId}/boards/{boardId}/cards/search")
    public ResponseEntity<Page<CardSearchResponseDto>> findAllByBoard(@PathVariable Long workspaceId,
                                                                      @PathVariable Long boardId,
                                                                      @RequestParam(required = false) String title,
                                                                      @RequestParam(required = false) String contents,
                                                                      @RequestParam(required = false) LocalDateTime startDate,
                                                                      @RequestParam(required = false) LocalDateTime endDate,
                                                                      @RequestParam(required = false) String assigneeName,
                                                                      Pageable pageable) {
        return ResponseEntity.ok().body(cardSearchService.findAllByBoard(workspaceId, boardId, title, contents, startDate, endDate, assigneeName, pageable));
    }
}
