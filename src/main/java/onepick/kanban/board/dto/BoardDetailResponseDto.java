package onepick.kanban.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import onepick.kanban.board.entity.Board;
import onepick.kanban.boardlist.dto.BoardListDetailResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardDetailResponseDto {

    private Long boardId;
    private String boardTitle;
    private List<BoardListDetailResponseDto> boardLists;

    public static BoardDetailResponseDto toDto(Board board) {
        List<BoardListDetailResponseDto> boardLists = board.getBoardLists().stream()
                .map(boardList -> BoardListDetailResponseDto.toDto(boardList))
                .collect(Collectors.toList());

        return new BoardDetailResponseDto(
                board.getId(),
                board.getTitle(),
                boardLists
        );
    }
}
