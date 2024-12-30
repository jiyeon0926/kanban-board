package onepick.kanban.boardlist.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onepick.kanban.boardlist.dto.BoardListRequestDto;
import onepick.kanban.boardlist.dto.BoardListResponseDto;
import onepick.kanban.boardlist.dto.EditBoardListRequestDto;
import onepick.kanban.boardlist.dto.UpdateBoardListRequestDto;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.boardlist.service.BoardListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/boards/{boardId}/lists")
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListService boardListService;

    // 리스트 생성
    @PostMapping
    public ResponseEntity<BoardListResponseDto> createList(@PathVariable Long boardId,
                                                           @Valid @RequestBody BoardListRequestDto requestDto) {
        BoardListResponseDto boardListResponseDto = boardListService.createList(boardId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardListResponseDto);
    }

    // 리스트 수정
    @PutMapping("/{listId}")
    public ResponseEntity<BoardListResponseDto> updateList(@PathVariable Long listId,
                                                           @RequestBody EditBoardListRequestDto requestDto) {
        BoardListResponseDto list = boardListService.updateList(listId, requestDto);
        return ResponseEntity.ok(list);
    }

    // 리스트 순서 변경
    @PatchMapping("/order")
    public ResponseEntity<String> updateListOrder(@PathVariable Long boardId, @RequestBody UpdateBoardListRequestDto dto) {
        boardListService.updateListOrder(boardId, dto);
        return ResponseEntity.ok("리스트 순서가 업데이트되었습니다.");
    }

    // 리스트 삭제
    @DeleteMapping("/{listId}")
    public ResponseEntity<String> deleteList(@PathVariable Long listId) {
        boardListService.deleteList(listId);
        return ResponseEntity.ok("리스트가 삭제되었습니다.");
    }
}