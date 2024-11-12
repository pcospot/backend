package net.yellowstrawberry.pcospot.db.repository;


import net.yellowstrawberry.pcospot.object.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
}
