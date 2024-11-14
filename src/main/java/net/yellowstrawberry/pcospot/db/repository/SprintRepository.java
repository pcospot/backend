package net.yellowstrawberry.pcospot.db.repository;

import net.yellowstrawberry.pcospot.object.sprint.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
}
