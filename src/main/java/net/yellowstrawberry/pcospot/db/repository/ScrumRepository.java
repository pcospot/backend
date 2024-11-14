package net.yellowstrawberry.pcospot.db.repository;

import net.yellowstrawberry.pcospot.object.sprint.Scrum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrumRepository extends JpaRepository<Scrum, Long> {
}
