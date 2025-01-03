package onepick.kanban.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onepick.kanban.card.entity.Card;
import onepick.kanban.common.Timestamp;
import onepick.kanban.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String emoji;

    public Comment(Card card, User user, String contents, String emoji) {
        this.card = card;
        this.user = user;
        this.contents = contents;
        this.emoji = emoji;
    }

    public void update(String contents, String emoji) {
        this.contents = contents;
        this.emoji = emoji;
    }
}
