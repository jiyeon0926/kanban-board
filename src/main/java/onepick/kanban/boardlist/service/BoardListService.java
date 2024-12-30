package onepick.kanban.boardlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.boardlist.dto.BoardListRequestDto;
import onepick.kanban.boardlist.dto.BoardListResponseDto;
import onepick.kanban.boardlist.dto.EditBoardListRequestDto;
import onepick.kanban.boardlist.dto.UpdateBoardListRequestDto;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.entity.Member;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.MemberRepository;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.user.service.MemberService;
import onepick.kanban.workspace.entity.Workspace;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.sound.midi.Sequence;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static onepick.kanban.board.entity.QBoard.board;

@Service
@RequiredArgsConstructor
public class BoardListService {

    private final BoardListRepository boardListRepository;
    private final BoardRepository boardRepository;
    private final SlackNotifier slackNotifier;
    private final UserRepository userRepository;
    private final MemberService memberService;

    public BoardListResponseDto createList(Long boardId, BoardListRequestDto requestDto) {

        Optional<Board> board = boardRepository.findById(boardId);

        if (board.isEmpty()) {
            throw new IllegalArgumentException("없는 보드입니다.");
        }

        checkRole(board.get());

        BoardList boardList = new BoardList(board.get(), requestDto.getTitle(), requestDto.getContents());

        String message = board.get().getTitle() + " 보드의 " + boardList.getTitle() + " 리스트가 생성되었습니다.";
        slackNotifier.sendNotification(message);

        return new BoardListResponseDto(boardListRepository.save(boardList));
    }

    public BoardListResponseDto updateList(Long listId, EditBoardListRequestDto requestDto) {
        BoardList boardList = boardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        checkRole(boardList.getBoard());

        boardList.update(requestDto.getTitle(), requestDto.getContents());
        return new BoardListResponseDto(boardListRepository.save(boardList));
    }

    @Transactional
    public void updateListOrder(Long boardId, UpdateBoardListRequestDto dto) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));

        checkRole(board);

        List<BoardList> boardList = board.getBoardLists();

        for (BoardList list : boardList) {
            if (list.getId() == dto.getListId()) {
                list.updateSequence(dto.getSequence());
            } else if (list.getSequence() < dto.getSequence())
                continue;
            else {
                list.addSequence();
            }
        }
    }

    public void deleteList(Long listId) {

        BoardList boardList = boardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        checkRole(boardList.getBoard());

        boardListRepository.delete(boardList);
    }

    private void checkRole(Board board) {

        Long workspaceId = board.getWorkspace().getId();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authEmail = auth.getName();
        User inviter = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));

        Member member = memberService.findByWorkspaceIdAndUserId(workspaceId, inviter.getId())
                .orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));

        if (member.getRole().equals(Role.READONLY)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
    }
}
