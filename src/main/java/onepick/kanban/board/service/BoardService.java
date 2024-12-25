//package onepick.kanban.board.service;
//
//import lombok.RequiredArgsConstructor;
//import onepick.kanban.board.dto.BoardRequestDto;
//import onepick.kanban.board.dto.BoardResponseDto;
//import onepick.kanban.board.entity.Board;
//import onepick.kanban.board.repository.BoardRepository;
//import onepick.kanban.workspace.entity.Workspace;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class BoardService {
//
//    private final BoardRepository boardRepository;
//
//    public BoardResponseDto createBoard(Long workspaceId, String title, String backgroundColor, String backgroundImage) {
//        if (title == null || title.isEmpty()) {
//            throw new IllegalArgumentException("제목을 입력해주세요.");
//        }
//        if (workspaceId == null || workspaceId <= 0) {
//            throw new IllegalArgumentException("유효하지 않은 워크스페이스 사용자입니다.");
//        }
//
//        Workspace workspace = new Workspace(workspaceId, "One Pick", "One Pick Content");
//        Board board = new Board(workspace, title, backgroundColor, backgroundImage);
//        board = boardRepository.save(board);
//
//        return new BoardResponseDto(board.getId(), board.getTitle(), board.getBackgroundColor(),board.getBackgroundImage(), null);
//    }
//
//    public List<BoardResponseDto> getBoards(Long workspaceId) {
//        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);
//        return boards.stream()
//                .map(board -> new BoardResponseDto(board.getId(), board.getTitle(), board.getBackgroundColor(), board.getBackgroundImage(), null))
//                .collect(Collectors.toList());
//    }
//
//    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto) {
//        Board board = boardRepository.findByBoardId(boardId)
//                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));
//
//        board.updateBoard(
//                requestDto.getTitle() != null ? requestDto.getTitle() : board.getTitle(),
//                requestDto.getBackgroundColor() != null ? requestDto.getBackgroundColor() : board.getBackgroundColor(),
//                requestDto.getBackgroundImage() != null ? requestDto.getBackgroundImage() : board.getBackgroundImage()
//        );
//
//        Board savedBoard = boardRepository.save(board);
//
//        return new BoardResponseDto(savedBoard.getId(), savedBoard.getTitle(), savedBoard.getBackgroundColor(), savedBoard.getBackgroundImage(), null);
//    }
//
//    public void deleteBoard(Long boardId) {
//        Board board = boardRepository.findByBoardId(boardId).orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));
//        boardRepository.delete(board);
//    }
//}
