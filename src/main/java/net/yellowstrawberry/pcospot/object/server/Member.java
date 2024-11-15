package net.yellowstrawberry.pcospot.object.server;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import net.yellowstrawberry.pcospot.utils.JsonToString;
import org.json.JSONObject;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Member {

    @Id
    private Long user;
    private Long serverid;
    private Long role;
    private Timestamp joined;
    @Convert(converter = JsonToString.class)
    private JSONObject seen;

}
