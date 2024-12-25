package onepick.kanban.board.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.dto.BoardRequestDto;
import onepick.kanban.board.dto.BoardResponseDto;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.card.entity.CardAttachment;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.workspace.entity.Workspace;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CardRepository cardRepository;

    public BoardResponseDto createBoard(Long workspaceId, String title, String backgroundColor, String backgroundImage) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (workspaceId == null || workspaceId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 워크스페이스 사용자입니다.");
        }

        Workspace workspace = new Workspace(workspaceId, "One Pick");
        Board board = new Board(workspace, title, backgroundColor, backgroundImage);
        board = boardRepository.save(board);

        return new BoardResponseDto(board.getId(), board.getTitle(), board.getBackgroundColor(),board.getBackgroundImage(), null);
    }
}
