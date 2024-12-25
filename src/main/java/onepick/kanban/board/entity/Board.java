package onepick.kanban.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onepick.kanban.boardlist.entity.BoardList;
import onepick.kanban.common.Timestamp;
import onepick.kanban.workspace.entity.Workspace;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String backgroundColor;

    private String backgroundImage;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardList> boardLists = new ArrayList<>();

    public Board(Workspace workspace, String title, String backgroundColor, String backgroundImage) {
        this.workspace = workspace;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImage = backgroundImage;
    }

    public void updateBoard(String title, String backgroundColor, String backgroundImage) {
        if(title != null && !title.isEmpty()) {
            this.title = title;
        }
        if(backgroundColor != null) {
            this.backgroundColor = backgroundColor;
        }
        if(backgroundImage != null) {
            this.backgroundImage = backgroundImage;
        }
    }
}
