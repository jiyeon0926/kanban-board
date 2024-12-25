package onepick.kanban.boardlist.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.boardlist.dto.BoardListRequestDto;
import onepick.kanban.boardlist.dto.BoardListResponseDto;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.boardlist.service.BoardListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/lists")
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListService boardListService;

    @PostMapping
    public ResponseEntity<BoardListResponseDto> createList(@PathVariable Long boardId,
                                                           @Valid @RequestBody BoardListRequestDto requestDto) {

        BoardListResponseDto boardListResponseDto = boardListService.createList(boardId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardListResponseDto);
    }



}