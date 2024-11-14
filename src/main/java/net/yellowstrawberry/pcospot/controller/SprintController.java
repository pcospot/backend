package net.yellowstrawberry.pcospot.controller;

import com.github.f4b6a3.tsid.Tsid;
import net.yellowstrawberry.pcospot.db.repository.ScrumRepository;
import net.yellowstrawberry.pcospot.db.repository.SprintRepository;
import net.yellowstrawberry.pcospot.object.sprint.Scrum;
import net.yellowstrawberry.pcospot.object.sprint.Sprint;
import net.yellowstrawberry.pcospot.object.user.AuthUser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;

@RestController
@RequestMapping("/sprint")
public class SprintController {

    private final SprintRepository sprintRepository;
    private final ScrumRepository scrumRepository;

    public SprintController(SprintRepository sprintRepository, ScrumRepository scrumRepository) {
        this.sprintRepository = sprintRepository;
        this.scrumRepository = scrumRepository;
    }

    @PutMapping("/{serverId}")
    public ResponseEntity<?> createSprint(@PathVariable Long serverId, String body, @AuthenticationPrincipal AuthUser principal) {
        Sprint sprint = buildSprint(serverId, principal.getAccount().getId(), body);
        return ResponseEntity.ok("{\"id\": %d}".formatted(sprint.getId()));
    }

    @PutMapping("/{serverId}/{sprintId}/scrum")
    public ResponseEntity<?> writeScrum(@PathVariable Long serverId, @PathVariable Long sprintId, @AuthenticationPrincipal AuthUser principal, String body) {
        JSONObject jsonObject = new JSONObject(body);
        Scrum scrum = buildScrum(serverId, sprintId, principal.getAccount().getId(), jsonObject.getString("content"));
        sprintRepository.findById(sprintId).ifPresent(sprint -> sprint.setStatus((short) jsonObject.getInt("status")));
        return ResponseEntity.ok("{\"id\": %d}".formatted(scrum.getId()));
    }

    private Sprint buildSprint(Long serverId, Long user, String body) {
        JSONObject jsonObject = new JSONObject(body);
        Sprint sprint = new Sprint(Tsid.fast().toLong(), user, serverId, new Date(jsonObject.getLong("date")), jsonObject.getString("content"), (short) 0);
        return sprintRepository.save(sprint);
    }

    private Scrum buildScrum(Long serverId, Long sprintId, Long user, String content) {
        Scrum scrum = new Scrum(Tsid.fast().toLong(), sprintId, serverId, user, content);
        return scrumRepository.save(scrum);
    }
}
