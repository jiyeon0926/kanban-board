package onepick.kanban.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import onepick.kanban.comment.dto.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CardResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final LocalDateTime deadline;
    private final List<CardAttachmentDto> attachments; // 첨부파일 DTO 리스트 추가
    private final List<CommentResponseDto> comments;
    private final List<CardHistoryDto> histories;
}
