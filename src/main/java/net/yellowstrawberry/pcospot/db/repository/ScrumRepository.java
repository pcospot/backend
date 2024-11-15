package net.yellowstrawberry.pcospot.db.repository;

import net.yellowstrawberry.pcospot.object.sprint.Scrum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrumRepository extends JpaRepository<Scrum, Long> {
    List<Scrum> findBySprintAndServer(Long sprint, Long server);
}
