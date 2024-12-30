package onepick.kanban.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CardSearchResponseDto {

    private Long id;
    private String title;
    private String contents;
    private LocalDateTime deadline;
}
