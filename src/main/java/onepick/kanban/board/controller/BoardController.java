//package onepick.kanban.board.controller;
//
//import lombok.RequiredArgsConstructor;
//import onepick.kanban.board.dto.BoardRequestDto;
//import onepick.kanban.board.dto.BoardResponseDto;
//import onepick.kanban.board.service.BoardService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/workspaces/{workspaceId}/boards")
//@RequiredArgsConstructor
//public class BoardController {
//
//    private final BoardService boardService;
//
//    // 보드 생성
////    @PostMapping
////    public ResponseEntity<BoardResponseDto> createBoard(@RequestParam Long workspaceId, @RequestParam String title,
////                                                        @RequestParam(required = false) String backgroundColor,
////                                                        @RequestParam(required = false) String backgroundImage) {
////        BoardResponseDto responseDto = boardService.createBoard(workspaceId, title, backgroundColor, backgroundImage);
////        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
////    }
//
//    // 보드 조회
//    @GetMapping
//    public ResponseEntity<List<BoardResponseDto>> getBoards(@RequestParam Long workspaceId) {
//        List<BoardResponseDto> boards = boardService.getBoards(workspaceId);
//        return ResponseEntity.ok(boards);
//    }
//
//    // 보드 수정
//    @PutMapping
//    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto requestDto) {
//        BoardResponseDto board = boardService.updateBoard(boardId, requestDto);
//        return ResponseEntity.ok(board);
//    }
//
//    // 보드 삭제
//    @DeleteMapping
//    public ResponseEntity<BoardResponseDto> deleteBoard(@PathVariable Long boardId) {
//        boardService.deleteBoard(boardId);
//        return ResponseEntity.noContent().build();
//    }
//}
