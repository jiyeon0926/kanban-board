package onepick.kanban.comment.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.comment.dto.CommentRequestDto;
import onepick.kanban.comment.entity.Comment;
import onepick.kanban.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards/{cardId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable Long cardId,
                                                @RequestBody CommentRequestDto requestDto) {
        Comment comment = commentService.createComment(cardId, requestDto);
        return ResponseEntity.ok(comment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId,
                                                 @RequestBody CommentRequestDto requestDto) {
        Comment updatedComment = commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
