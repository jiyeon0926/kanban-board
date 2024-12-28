package onepick.kanban.card.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CardHistoryDto {

    private Long id;
    private String logMessage;
    private LocalDateTime createdAt;
}