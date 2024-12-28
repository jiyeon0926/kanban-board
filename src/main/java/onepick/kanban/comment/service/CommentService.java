package onepick.kanban.comment.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.comment.dto.CommentRequestDto;
import onepick.kanban.comment.dto.CommentResponseDto;
import onepick.kanban.comment.entity.Comment;
import onepick.kanban.comment.repository.CommentRepository;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final SlackNotifier slackNotifier;

    public CommentResponseDto createComment(Long cardId, CommentRequestDto requestDto) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));

        User user = userRepository.findById(card.getId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Comment comment = new Comment(card, user, requestDto.getContents(), requestDto.getEmoji());

        String message = comment.getUser().getName() + " 멤버가 " + comment.getCard().getTitle() + " 카드에 댓글을 남겼습니다.";
        slackNotifier.sendNotification(message);
        commentRepository.save(comment);
        return new CommentResponseDto(comment.getId(), comment.getUser().getName(), comment.getContents(), comment.getEmoji(), comment.getCard().getId(), comment.getCreatedAt(), comment.getModifiedAt());
    }

    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.update(requestDto.getContents(), requestDto.getEmoji());
        commentRepository.save(comment);
        return new CommentResponseDto(comment.getId(), comment.getUser().getName(), comment.getContents(), comment.getEmoji(), comment.getCard().getId(), comment.getCreatedAt(), comment.getModifiedAt());
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }
}
