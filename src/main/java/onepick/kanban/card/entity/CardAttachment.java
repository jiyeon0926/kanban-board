package onepick.kanban.card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CardAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;
    private String imageName;
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public CardAttachment(Card card, String image, String imageName, String fileType) {
        this.card = card;
        this.image = image;
        this.imageName = imageName;
        this.fileType = fileType;
    }
}
