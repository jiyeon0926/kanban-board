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
import org.springframework.stereotype.Service;

import javax.sound.midi.Sequence;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardListService {

    private final BoardListRepository boardListRepository;
    private final BoardRepository boardRepository;
    private final SlackNotifier slackNotifier;

    public BoardListResponseDto createList(Long boardId, BoardListRequestDto requestDto) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("없는 보드입니다."));
        BoardList boardList = new BoardList(board, requestDto.getTitle(), requestDto.getContents());

        String message = board.getTitle() + " 보드의 " + boardList.getTitle() + " 리스트가 생성되었습니다.";
        slackNotifier.sendNotification(message);

        return new BoardListResponseDto(boardListRepository.save(boardList));
    }

    public BoardListResponseDto updateList(Long listId, EditBoardListRequestDto requestDto) {
        BoardList boardList = boardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        boardList.update(requestDto.getTitle(), requestDto.getContents());
        return new BoardListResponseDto(boardListRepository.save(boardList));
    }

    @Transactional
    public void updateListOrder(Long boardId, UpdateBoardListRequestDto dto) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));

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
        boardListRepository.delete(boardList);
    }
}
