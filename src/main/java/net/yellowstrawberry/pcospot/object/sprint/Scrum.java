package net.yellowstrawberry.pcospot.object.sprint;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Scrum {
    @Id
    private Long id;
    private Long sprint;
    private Long server;
    private Long user;
    private String content;

    public Scrum() {}

    public Scrum(Long id, Long sprint, Long server, Long user, String content) {
        this.id = id;
        this.sprint = sprint;
        this.server = server;
        this.user = user;
        this.content = content;
    }

    @ManyToOne
    @JoinColumn(name="sprint", nullable=false)
    private Sprint sprintObject;
}
