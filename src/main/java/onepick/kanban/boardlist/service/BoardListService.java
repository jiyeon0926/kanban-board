package onepick.kanban.boardlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.boardlist.dto.BoardListRequestDto;
import onepick.kanban.boardlist.dto.BoardListResponseDto;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.common.SlackNotifier;
import org.springframework.stereotype.Service;

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

    public BoardListResponseDto updateList(Long listId, BoardListRequestDto requestDto) {
        BoardList boardList = boardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        boardList.update(requestDto.getTitle(), requestDto.getOrder());
        return new BoardListResponseDto(boardListRepository.save(boardList));
    }

    // 리스트 순서 변경 미완성
    public void updateListOrder(Long boardId, Map<Long, Integer> listOrderMap) {
        listOrderMap.forEach((listId, newOrder) -> {
        });
    }

    public void deleteList(Long listId) {
        BoardList boardList = boardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));
        boardListRepository.delete(boardList);
    }
}
