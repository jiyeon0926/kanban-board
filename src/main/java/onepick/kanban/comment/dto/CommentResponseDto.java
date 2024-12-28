package onepick.kanban.comment.dto;

import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String contents;
    private final String name;
    private final Long cardId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CommentResponseDto(Long id, String contents, String name, Long cardId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.contents = contents;
        this.name = name;
        this.cardId = cardId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
