package onepick.kanban.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardAttachmentDto {

    private final Long id;
    private final String image;
    private final String imageName;
    private final String fileType;
}
