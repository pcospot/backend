package net.yellowstrawberry.pcospot.object.recruitment;

import com.github.f4b6a3.tsid.Tsid;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Date;
import java.util.List;

@Document("recruitment")
@Getter
@Setter
@EntityListeners(RecruitmentListener.class)
public class Recruitment {

    @Id
    private Long recruitmentId;
    private String projectName;
    private float[] vectorEmbedding;
    private String description;
    private Date startDate;
    private Date endDate;
    private List<String> rolesNeeded;
    private String requirements;



    public Recruitment() {}

    public Recruitment(String projectName, String description, Date startDate, Date endDate, List<String> rolesNeeded, String requirements) {
        this.recruitmentId = Tsid.fast().toLong();
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rolesNeeded = rolesNeeded;
        this.requirements = requirements;
    }
}
