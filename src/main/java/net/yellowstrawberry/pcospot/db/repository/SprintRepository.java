package net.yellowstrawberry.pcospot.db.repository;

import net.yellowstrawberry.pcospot.object.sprint.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByServer(Long server);

    Sprint[] findByServerAndStatus(Long server, Short status);
}
