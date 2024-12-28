package onepick.kanban.board.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.dto.BoardDetailResponseDto;
import onepick.kanban.board.dto.BoardRequestDto;
import onepick.kanban.board.dto.BoardResponseDto;
import onepick.kanban.board.service.BoardService;
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

import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@PathVariable Long workspaceId,
                                                              @RequestBody BoardRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoard(workspaceId, requestDto));
    }

    // 보드 다건 조회
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getBoards(@PathVariable Long workspaceId,
                                                            @AuthenticationPrincipal User user) {
        List<BoardResponseDto> boards = boardService.getBoards(workspaceId, user);

        return ResponseEntity.ok(boards);
    }

    // 보드 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponseDto> getBoard(@PathVariable Long workspaceId,
                                                           @PathVariable Long boardId) {
        return ResponseEntity.ok().body(boardService.getBoard(workspaceId, boardId));
    }

    // 보드 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId,
                                                        @RequestBody BoardRequestDto requestDto,
                                                        @AuthenticationPrincipal User user) {
        BoardResponseDto board = boardService.updateBoard(boardId, requestDto, user);

        return ResponseEntity.ok(board);
    }

    // 보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId,
                                            @AuthenticationPrincipal User user) {
        boardService.deleteBoard(boardId, user);

        return ResponseEntity.noContent().build();
    }
}
