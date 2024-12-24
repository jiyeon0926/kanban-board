package onepick.kanban.comment.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.comment.service.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
}
