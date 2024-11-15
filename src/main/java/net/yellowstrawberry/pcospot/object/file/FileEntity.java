package net.yellowstrawberry.pcospot.object.file;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalFilename;
    private String type;

    public FileEntity() {}

    public FileEntity(Long id, String originalFilename, String type) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.type = type;
    }

}
