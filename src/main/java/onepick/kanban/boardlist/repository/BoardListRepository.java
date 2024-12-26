package onepick.kanban.boardlist.repository;

import onepick.kanban.boardlist.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardListRepository extends JpaRepository<BoardList, Long> {

}