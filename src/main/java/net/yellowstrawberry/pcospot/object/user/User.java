package net.yellowstrawberry.pcospot.object.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class User {

    @Id
    private Long id;
    private String userid;
    private String username;
    private String nickname;
    private String description;
    private String password;
    private Timestamp joined;
}
