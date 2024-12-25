package onepick.kanban.boardlist.dto;

import lombok.Getter;
import onepick.kanban.boardlist.entity.BoardList;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String title;
    private Integer sequence;

    public BoardListResponseDto(BoardList boardList) {
        this.id = boardList.getId();
        this.title = boardList.getTitle();
        this.sequence = boardList.getSequence();
    }
}
