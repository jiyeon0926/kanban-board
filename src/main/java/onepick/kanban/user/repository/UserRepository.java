package onepick.kanban.user.repository;

import onepick.kanban.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default User findByIdOrElseThrow(Long userId) {
        return findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 데이터가 존재하지 않습니다."));
    }

    List<User> findAllByEmailIn(List<String> emails);
}