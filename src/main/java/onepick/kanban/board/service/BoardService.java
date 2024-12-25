package onepick.kanban.board.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.dto.BoardRequestDto;
import onepick.kanban.board.dto.BoardResponseDto;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto requestDto) {

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));

        Board board = new Board(workspace, requestDto.getTitle(), requestDto.getBackgroundColor(), requestDto.getBackgroundImage());
        board = boardRepository.save(board);
        return new BoardResponseDto(board.getId(), board.getTitle(), board.getBackgroundColor(), board.getBackgroundImage(), null);
        }

    public List<BoardResponseDto> getBoards(Long workspaceId) {
        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);
        return boards.stream()
                .map(board -> new BoardResponseDto(board.getId(), board.getTitle(), board.getBackgroundColor(), board.getBackgroundImage(), null))
                .collect(Collectors.toList());
    }

    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto) {
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));

        board.updateBoard(requestDto.getTitle(), requestDto.getBackgroundColor(), requestDto.getBackgroundImage());
        Board savedBoard = boardRepository.save(board);

        return new BoardResponseDto(savedBoard.getId(), savedBoard.getTitle(), savedBoard.getBackgroundColor(), savedBoard.getBackgroundImage(), null);
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));
        boardRepository.delete(board);
    }
}
