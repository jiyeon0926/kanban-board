package onepick.kanban.boardlist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardListRequestDto {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    private String contents;
    private Integer order;

}