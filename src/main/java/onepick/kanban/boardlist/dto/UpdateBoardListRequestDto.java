package onepick.kanban.boardlist.dto;

import lombok.Getter;

@Getter
public class UpdateBoardListRequestDto {
    private Long listId;
    private Integer order;
}
