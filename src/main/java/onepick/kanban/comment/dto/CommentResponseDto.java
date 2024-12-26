package onepick.kanban.comment.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String contents;
    private final String user;
    private final Long cardId;
    private final String createdAt;
    private final String updatedAt;

    public CommentResponseDto(Long id, String contents, String user, Long cardId, String createdAt, String updatedAt) {
        this.id = id;
        this.contents = contents;
        this.user = user;
        this.cardId = cardId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
