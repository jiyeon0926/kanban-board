package onepick.kanban.card.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.comment.entity.Comment;
import onepick.kanban.common.Timestamp;
import onepick.kanban.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Card extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    private Integer order = 1;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private BoardList boardList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE)
    private List<CardAttachment> cardAttachments = new ArrayList<>();

    public Card(BoardList boardList, User user, String title, String contents, LocalDateTime deadline) {
        this.boardList = boardList;
        this.user = user;
        this.title =title;
        this.contents = contents;
        this.deadline = deadline;
    }

    public void updateCard(String title, String contents, LocalDateTime deadline) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
        if (contents != null) {
            this.contents = contents;
        }
        if (deadline != null) {
            this.deadline = deadline;
        }
    }
}
