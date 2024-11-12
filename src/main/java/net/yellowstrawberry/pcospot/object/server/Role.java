package net.yellowstrawberry.pcospot.object.server;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role {
    @Id
    private Long id;
    private Long server;
    private String name;
    private Integer permission;
    private Integer color;

    public Role() {}
    public Role(Long id, Long server, String name, Integer permission, Integer color) {
        this.id = id;
        this.server = server;
        this.name = name;
        this.permission = permission;
        this.color = color;
    }
}
