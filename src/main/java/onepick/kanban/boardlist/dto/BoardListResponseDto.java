package onepick.kanban.boardlist.dto;

import lombok.Getter;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.card.dto.CardResponseDto;

import java.util.List;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String title;
    private String contents;
    private Integer sequence;
    private List<CardResponseDto> cards;

    public BoardListResponseDto(BoardList boardList) {
        this.id = boardList.getId();
        this.title = boardList.getTitle();
        this.sequence = boardList.getSequence();
    }
}
