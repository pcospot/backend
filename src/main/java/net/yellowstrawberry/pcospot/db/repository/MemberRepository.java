package net.yellowstrawberry.pcospot.db.repository;

import net.yellowstrawberry.pcospot.object.server.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
