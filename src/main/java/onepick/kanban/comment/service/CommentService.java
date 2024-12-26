package onepick.kanban.comment.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.comment.dto.CommentRequestDto;
import onepick.kanban.comment.entity.Comment;
import onepick.kanban.comment.repository.CommentRepository;
import onepick.kanban.user.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public Comment createComment(Long cardId, CommentRequestDto requestDto) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));

        User user = userRepository.finById(userId);

        Comment comment = new Comment(card, user, requestDto.getContents(), requestDto.getEmoji());
        return commentRepository.save(comment);
    }

}
