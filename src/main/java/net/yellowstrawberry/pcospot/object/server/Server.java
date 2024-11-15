package net.yellowstrawberry.pcospot.object.server;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Server {

    @Id
    private Long id;
    private String name;
    private Long owner;
    private Timestamp createdAt;
    private Long end;
    private Long image;
    private boolean invite;

    @Transient
    private long lastUpdate;

    public Server() {

    }
}
