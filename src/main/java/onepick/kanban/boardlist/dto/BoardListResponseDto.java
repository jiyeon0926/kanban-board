package onepick.kanban.boardlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.card.dto.CardResponseDto;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardListResponseDto {
    private Long id;
    private String title;
    private String contents;
    private Integer sequence;

    public BoardListResponseDto(BoardList boardList) {
        this.id = boardList.getId();
        this.title = boardList.getTitle();
        this.contents = boardList.getContents();
        this.sequence = boardList.getSequence();
    }
}
