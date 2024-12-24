package onepick.kanban.boardlist.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.boardlist.repository.BoardListRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListRepository boardListRepository;
}