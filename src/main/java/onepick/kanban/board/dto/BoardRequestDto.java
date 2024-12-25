package onepick.kanban.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onepick.kanban.boardlist.dto.BoardListResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {

    private Long id;
    private String title;
    private String backgroundColor;
    private String backgroundImage;
    private List<BoardListResponseDto> boardList;
}
