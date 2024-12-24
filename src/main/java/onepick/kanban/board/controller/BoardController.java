package onepick.kanban.board.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.service.BoardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
}
