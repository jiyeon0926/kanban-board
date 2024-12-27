package onepick.kanban.user.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import onepick.kanban.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    default User findByIdOrElseThrow(Long userId) {
        return findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 데이터가 존재하지 않습니다."));
    }

    List<User> findAllByEmail(@NotEmpty(message = "초대할 이메일 리스트를 입력해야 합니다.") List<@Email(message = "유효한 이메일 형식이어야 합니다.") String> emails);

}