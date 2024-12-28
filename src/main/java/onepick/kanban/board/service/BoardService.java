package onepick.kanban.board.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.dto.BoardDetailResponseDto;
import onepick.kanban.board.dto.BoardRequestDto;
import onepick.kanban.board.dto.BoardResponseDto;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import onepick.kanban.workspace.entity.Status;
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

//    @Transactional
//    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto requestDto, User user) {
//        validateUserCanModify(user, workspaceId);
//
//        Workspace workspace = workspaceRepository.findById(workspaceId)
//                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));
//
//        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
//            throw new IllegalArgumentException("제목은 필수 입력 항목입니다.");
//        }
//
//        Board board = new Board(workspace, user, requestDto.getTitle(), requestDto.getBackgroundColor(), requestDto.getBackgroundImage());
//        board = boardRepository.save(board);
//
//        String message = workspace.getTitle() + " 워크스페이스의 " + board.getTitle() + " 보드가 생성되었습니다.";
//        slackNotifier.sendNotification(message);
//
//        return mapToBoardResponseDto(board);
//    }

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
    public List<BoardResponseDto> getBoards(Long workspaceId, User user) {
        validateUserWorkspaceAccess(user, workspaceId);
        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);
        return boards.stream()
                .map(this::mapToBoardResponseDto)
                .collect(Collectors.toList());
    }

//    기존 단건 조회 로직 : 예외 처리 수정
//    @Transactional(readOnly = true)
//    public BoardDetailResponseDto getBoard(Long workspaceId, Long boardId, User user) {
//        validateUserWorkspaceAccess(user, workspaceId);
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));
//        if (!board.getWorkspace().getId().equals(workspaceId)) {
//            throw new IllegalArgumentException("보드가 해당 워크스페이스에 속하지 않습니다.");
//        }
//        return mapToBoardDetailResponseDto(board);
//    }

    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoard(Long workspaceId, Long boardId) {
        Board board = boardRepository.findByBoardId(workspaceId, boardId);
        return BoardDetailResponseDto.toDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user) {
        validateUserCanModify(user, boardId);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));

        board.updateBoard(requestDto.getTitle(), requestDto.getBackgroundColor(), requestDto.getBackgroundImage());
        Board savedBoard = boardRepository.save(board);

        return mapToBoardResponseDto(savedBoard);
    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        validateUserCanModify(user, boardId);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("보드를 찾을 수 없습니다."));
        boardRepository.delete(board);
    }

    // 권한 검증: READONLY는 수정/삭제 불가
    private void validateUserCanModify(User user, Long identifier) {
        if (user.getRole() == Role.READONLY) {
            throw new SecurityException("READONLY 권한으로는 수정/삭제가 불가능합니다.");
        }
        // 추가적인 워크스페이스 권한 검증 가능
    }

    private void validateUserWorkspaceAccess(User user, Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));

        if (!workspace.getInvites().stream().anyMatch(invite ->
                invite.getInvitee().equals(user) && invite.getStatus() == Status.ACCEPTED)) {
            throw new SecurityException("워크스페이스에 접근할 권한이 없습니다.");
        }
    }

    private BoardResponseDto mapToBoardResponseDto(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getBackgroundImage()
//                null // boardList은 다건 조회에서 제외
        );
    }

//    private BoardDetailResponseDto mapToBoardDetailResponseDto(Board board) {
//        List<BoardListResponseDto> boardLists = board.getBoardLists().stream()
//                .map(boardList -> new BoardListResponseDto(
//                        boardList.getId(),
//                        boardList.getTitle(),
//                        boardList.getContents(),
//                        boardList.getSequence(),
//                        boardList.getCards().stream().map(card -> new CardResponseDto(
//                                card.getId(),
//                                card.getTitle(),
//                                card.getContents(),
//                                card.getDeadline(),
//                                card.getCardAttachments().stream()
//                                        .map(att -> new CardAttachmentDto(att.getId(), att.getImage(), att.getImageName(), att.getFileType()))
//                                        .collect(Collectors.toList()),
//                                card.getComments().stream()
//                                        .map(c -> new CommentResponseDto(c.getId(), c.getContents(), c.getEmoji(), c.getUser().getName(), c.getCreatedAt()))
//                                        .collect(Collectors.toList()),
//                                card.getCardHistories().stream()
//                                        .map(h -> new CardHistoryDto(h.getId(), h.getLogMessage(), h.getCreatedAt()))
//                                        .collect(Collectors.toList())
//                        )).collect(Collectors.toList())
//                ))
//                .collect(Collectors.toList());
//
//        return new BoardDetailResponseDto(
//                board.getId(),
//                board.getTitle(),
//                board.getBackgroundColor(),
//                board.getBackgroundImage(),
//                boardLists
//        );
//    }
}
