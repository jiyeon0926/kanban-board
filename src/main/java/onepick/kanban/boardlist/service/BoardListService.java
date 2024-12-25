package onepick.kanban.boardlist.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.entity.Board;
import onepick.kanban.board.repository.BoardRepository;
import onepick.kanban.boardlist.dto.BoardListRequestDto;
import onepick.kanban.boardlist.dto.BoardListResponseDto;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.boardlist.repository.BoardListRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardListService {

    private final BoardListRepository boardListRepository;
    private final BoardRepository boardRepository;

    public BoardListResponseDto createList(Long boardId, BoardListRequestDto requestDto) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("없는 보드입니다."));
        BoardList boardList = new BoardList(board, requestDto.getTitle(), requestDto.getContents());
        return new BoardListResponseDto(boardListRepository.save(boardList));
    }

    public BoardListResponseDto updateList(Long listId, BoardListRequestDto requestDto) {
        BoardList boardList = boardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("리스트를 찾을 수 없습니다."));

        boardList.update(requestDto.getTitle(), requestDto.getOrder());
        return new BoardListResponseDto(boardListRepository.save(boardList));
    }
}
