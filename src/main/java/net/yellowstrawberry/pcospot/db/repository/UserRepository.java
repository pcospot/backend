package net.yellowstrawberry.pcospot.db.repository;

import net.yellowstrawberry.pcospot.object.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
