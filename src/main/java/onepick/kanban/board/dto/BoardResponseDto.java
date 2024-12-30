package onepick.kanban.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onepick.kanban.board.entity.Board;
import onepick.kanban.boardlist.dto.BoardListResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String title;
    private String backgroundColor;
    private String backgroundImage;
//    private List<BoardListResponseDto> boardList;

    public static BoardResponseDto toDto(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getBackgroundImage()
        );
    }
}






