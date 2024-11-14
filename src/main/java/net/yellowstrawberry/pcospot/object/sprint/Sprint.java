package net.yellowstrawberry.pcospot.object.sprint;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Sprint {

    @Id
    private Long id;
    private Long user;
    private Long server;
    private Date date;
    private String content;
    private Short status;

    @OneToMany(mappedBy="sprintObject")
    private Set<Scrum> scrums;

    public Sprint() {}

    public Sprint(Long id, Long user, Long server, Date date, String content, Short status) {
        this.id = id;
        this.user = user;
        this.server = server;
        this.date = date;
        this.content = content;
        this.status = status;
    }
}
