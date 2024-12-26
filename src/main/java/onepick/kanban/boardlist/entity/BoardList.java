package onepick.kanban.boardlist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onepick.kanban.board.entity.Board;
import onepick.kanban.card.entity.Card;
import onepick.kanban.common.Timestamp;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class BoardList extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String contents;

    private Integer sequence = 1;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "boardList", cascade = CascadeType.REMOVE)
    private List<Card> cards = new ArrayList<>();

    public BoardList(Board board, String title, String contents) {
        this.board = board;
        this.title =title;
        this.contents = contents;
    }

    public void update(String title, Integer sequence) {
        this.title = title;
        this.sequence = sequence;
    }

    public void addSequence() {
        this.sequence++;
    }
}
