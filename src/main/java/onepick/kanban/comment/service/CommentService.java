package onepick.kanban.comment.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
}
