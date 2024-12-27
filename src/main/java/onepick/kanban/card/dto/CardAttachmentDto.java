package onepick.kanban.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardAttachmentDto {

    private final Long id;
    private final String image;
    private final String image_name;
    private final String file_type;
}
