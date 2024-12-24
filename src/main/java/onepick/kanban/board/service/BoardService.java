package onepick.kanban.board.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
}
