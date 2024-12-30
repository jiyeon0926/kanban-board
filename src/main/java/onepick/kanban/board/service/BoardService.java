package onepick.kanban.board.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.dto.BoardDetailResponseDto;
import onepick.kanban.board.dto.BoardRequestDto;
import onepick.kanban.board.dto.BoardResponseDto;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.repository.WorkspaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final SlackNotifier slackNotifier;

    @Transactional
    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto boardRequestDto) {

        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);

        if (workspace.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "워크스페이스가 없습니다.");
        }

        Board board = new Board(workspace.get(), boardRequestDto.getTitle(), boardRequestDto.getBackgroundColor(), boardRequestDto.getBackgroundImage());
        boardRepository.save(board);

        String message = workspace.get().getTitle() + " 워크스페이스의 " + board.getTitle() + " 보드가 생성되었습니다.";
        slackNotifier.sendNotification(message);

        return BoardResponseDto.toDto(board);
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards(Long workspaceId) {

        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);

        return boards.stream()
                .map(this::mapToBoardResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoard(Long workspaceId, Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));

        return BoardDetailResponseDto.toDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));

        board.updateBoard(requestDto.getTitle(), requestDto.getBackgroundColor(), requestDto.getBackgroundImage());
        Board savedBoard = boardRepository.save(board);

        return mapToBoardResponseDto(savedBoard);
    }

    @Transactional
    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));
        boardRepository.delete(board);
    }

    private BoardResponseDto mapToBoardResponseDto(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getBackgroundImage()
        );
    }
}
