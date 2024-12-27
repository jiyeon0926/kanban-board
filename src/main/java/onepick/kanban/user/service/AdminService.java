package onepick.kanban.user.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.user.dto.RoleResponseDto;
import onepick.kanban.user.dto.UserResponseDto;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional
    public RoleResponseDto updateRole(Long userId, String role) {
        User user = userRepository.findByIdOrElseThrow(userId);

        if (user.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "관리자의 권한을 수정할 수 없습니다.");
        }

        user.updateRole(role);
        userRepository.save(user);

        return RoleResponseDto.toDto(user);
    }

    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponseDto::toDto)
                .toList();
    }
}
