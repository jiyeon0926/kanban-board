package onepick.kanban.boardlist.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.boardlist.dto.BoardListRequestDto;
import onepick.kanban.boardlist.dto.BoardListResponseDto;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.boardlist.repository.BoardListRepository;
import onepick.kanban.common.SlackNotifier;
import org.springframework.stereotype.Service;

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
}
