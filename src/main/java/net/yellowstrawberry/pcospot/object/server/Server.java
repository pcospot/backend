package net.yellowstrawberry.pcospot.object.server;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Server {

    @Id
    private Long id;
    private String name;
    private Long owner;
    private Timestamp createdAt;
    private Long end;
}
