package net.yellowstrawberry.pcospot.object.user;

import com.github.f4b6a3.tsid.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    private String googleId;
    private String username;
    private String nickname;
    private String description;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Timestamp joined;

    public User() {}

    public User(String googleId, String username, String nickname, String description) {
        id = Tsid.fast().toLong();
        this.googleId = googleId;
        this.username = username;
        this.nickname = nickname;
        this.description = description;
    }
}
