package onepick.kanban.boardlist.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.boardlist.repository.BoardListRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardListService {

    private final BoardListRepository boardListRepository;
}
