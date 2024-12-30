package onepick.kanban.comment.dto;

import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String name;
    private final String contents;
    private final String emoji;
    private final Long cardId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CommentResponseDto(Long id, String name, String contents, String emoji, Long cardId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.contents = contents;
        this.emoji = emoji;
        this.cardId = cardId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
