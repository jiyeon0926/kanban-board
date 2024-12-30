package onepick.kanban.user.repository;

import onepick.kanban.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member findByIdOrElseThrow(Long userId) {
        return findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 데이터가 존재하지 않습니다."));
    }

    @Query("SELECT m FROM Member m INNER JOIN FETCH m.workspace w WHERE w.id = :workspaceId AND m.id = :memberId")
    Member findByWorkspaceIdAndMemberId(@Param("workspaceId") Long workspaceId, @Param("memberId") Long memberId);

    Optional<Member> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);
}
