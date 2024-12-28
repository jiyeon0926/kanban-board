package onepick.kanban.boardlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import onepick.kanban.board.dto.CardDto;
import onepick.kanban.boardlist.entity.BoardList;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardListDetailResponseDto {

    private Long listId;
    private String title;
    private List<CardDto> cards;

    public static BoardListDetailResponseDto toDto(BoardList boardList) {
        List<CardDto> cards = boardList.getCards().stream()
                .map(card -> new CardDto(card.getId(), card.getTitle()))
                .collect(Collectors.toList());

        return new BoardListDetailResponseDto(
                boardList.getId(),
                boardList.getTitle(),
                cards
        );
    }
}

