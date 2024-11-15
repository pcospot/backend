package net.yellowstrawberry.pcospot.object.recruitment;

import jakarta.persistence.PrePersist;
import net.yellowstrawberry.pcospot.service.EmbeddingService;
import org.springframework.stereotype.Component;

@Component
public class RecruitmentListener {
    private final EmbeddingService service;

    public RecruitmentListener(EmbeddingService service) {
        this.service = service;
    }

    @PrePersist
    public void embed(Recruitment recruitment){
        recruitment.setVectorEmbedding(service.embed(recruitment.getDescription()));
    }
}
