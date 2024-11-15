package net.yellowstrawberry.pcospot.db.repository;

import net.yellowstrawberry.pcospot.object.file.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
}
