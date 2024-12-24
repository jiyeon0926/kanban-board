package onepick.kanban.user.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
}
