package onepick.kanban.comment.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.comment.dto.CommentRequestDto;
import onepick.kanban.comment.entity.Comment;
import onepick.kanban.comment.repository.CommentRepository;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public Comment createComment(Long cardId, CommentRequestDto requestDto) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));

        Optional<User> user = userRepository.findById(card.getUser().getId());

        Comment comment = new Comment(card, user.get(), requestDto.getContents(), requestDto.getEmoji());
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.update(requestDto.getContents(), requestDto.getEmoji());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }
}
