package onepick.kanban.user.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
}
