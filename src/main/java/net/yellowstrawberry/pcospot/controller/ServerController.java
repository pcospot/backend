package net.yellowstrawberry.pcospot.controller;

import com.github.f4b6a3.tsid.Tsid;
import com.google.gson.Gson;
import net.yellowstrawberry.pcospot.db.MessageManager;
import net.yellowstrawberry.pcospot.db.repository.MemberRepository;
import net.yellowstrawberry.pcospot.db.repository.RoleRepository;
import net.yellowstrawberry.pcospot.db.repository.ServerRepository;
import net.yellowstrawberry.pcospot.object.server.Member;
import net.yellowstrawberry.pcospot.object.server.Role;
import net.yellowstrawberry.pcospot.object.user.AuthUser;
import net.yellowstrawberry.pcospot.service.StoreService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/server/")
public class ServerController {

    private final Gson gson = new Gson();
    private final ServerRepository serverRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final StoreService storeService;

    public ServerController(ServerRepository serverRepository, MemberRepository memberRepository, RoleRepository roleRepository, StoreService storeService) {
        this.serverRepository = serverRepository;
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.storeService = storeService;
    }

    @GetMapping("/{id}/")
    public ResponseEntity<?> fetchInfo(@PathVariable Long id, @AuthenticationPrincipal AuthUser principal) {
        Member m = memberRepository.findById(principal.getAccount().getId()).orElse(null);
        if(m == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return serverRepository.findById(id)
                .map((server) -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", server.getName());
                    jsonObject.put("created_at", server.getCreatedAt().getTime());
                    jsonObject.put("end_on", server.getEnd());

                    JSONObject notification = new JSONObject();
                    notification.put("new", MessageManager.isThereNewMessage(
                            m.getSeen().has(String.valueOf(id)) ?
                                m.getSeen().getLong(String.valueOf(id))
                                : 0,
                            id
                    ));
                    notification.put("mentions", 0);
                    jsonObject.put("notification", notification);

                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
                }).orElse(
                        new ResponseEntity<>(HttpStatus.NOT_FOUND)
                );
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<?> fetchImage(@PathVariable Long id) {
        return serverRepository.findById(id)
                .map((server) -> new ResponseEntity<>(storeService.load(server.getImage()), HttpStatus.OK)).orElse(
                        new ResponseEntity<>(HttpStatus.NOT_FOUND)
                );
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> setImage(@PathVariable Long id, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal AuthUser principal) {
        if(checkAdmin(id, principal.getAccount().getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return serverRepository.findById(id)
                .map((server) -> ResponseEntity.ok("{\"id\": %d}".formatted(storeService.store(file)))).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/server/{id}/{channel}/messages")
    public ResponseEntity<?> fetchMessages(@PathVariable Long id, @PathVariable Long channel, @RequestParam(value = "before") Long before, @RequestParam(value = "after") Long after, @RequestParam(value = "max") Integer max) {
        JSONObject o = new JSONObject();

        JSONArray a = MessageManager.loadMessages(
                id,
                channel,
                before==null?System.currentTimeMillis():before,
                after==null?0:after,
                max==null?50:max
        );
        if (a != null) {
            o.put("messages", a);
            o.put("last_update", serverRepository.findById(id).get().getLastUpdate());

            return new ResponseEntity<>(o.toString(), HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/server/{id}/role/{roleId}")
    public ResponseEntity<?> fetchRole(@PathVariable Long id, @PathVariable Long roleId) {
        return roleRepository.findRoleByIdAndServer(roleId, id).map((Role) -> new ResponseEntity<>(gson.toJson(Role), HttpStatus.CREATED)).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping("/server/{id}/role/{roleId}")
    public ResponseEntity<?> editRole(@PathVariable Long id, @PathVariable Long roleId, String body, @AuthenticationPrincipal AuthUser principal) {
        if(checkAdmin(id, principal.getAccount().getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        JSONObject o = new JSONObject(body);
        return roleRepository.findRoleByIdAndServer(id, roleId).map(r -> {
            if(o.has("name")) r.setName(o.getString("name"));
            if(o.has("permission")) r.setPermission(o.getInt("permission"));
            if(o.has("color")) r.setColor(o.getInt("color"));

            return new ResponseEntity<>(HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/server/{id}/role")
    public ResponseEntity<?> createRole(@PathVariable Long id, String body, @AuthenticationPrincipal AuthUser principal) {
        if(checkAdmin(id, principal.getAccount().getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Role r = buildRoleOutOfJSON(id, body);
        roleRepository.save(r);

        return new ResponseEntity<>(gson.toJson(r), HttpStatus.OK);
    }

    @PutMapping("/server/{id}/role/{role}/to/{user}")
    public ResponseEntity<?> applyRole(@PathVariable Long id, @PathVariable Long role, @PathVariable Long user, @AuthenticationPrincipal AuthUser principal) {
        if(checkAdmin(id, principal.getAccount().getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Member m = memberRepository.findByServeridAndUser(id, user).get(0);
        m.setRole(role);
        memberRepository.save(m);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean checkAdmin(Long server, Long user) {
        Optional<Role> r = roleRepository.findRoleByIdAndServer(memberRepository.findByServeridAndUser(server, user).get(0).getRole(), server);
        if(r.isPresent() && r.get().getPermission() == 1<<2) return true;

        return serverRepository.findById(server).get().getOwner().longValue()==user;
    }

    private Role buildRoleOutOfJSON(long server, String body) {
        JSONObject o = new JSONObject(body);
        return new Role(Tsid.fast().toLong(), server, o.getString("name"), o.getInt("permission"), o.getInt("color"));
    }

    @PostMapping("/server/{id}/{channelId}/message")
    public ResponseEntity<?> sendMessage(@PathVariable Long id, @PathVariable Long channelId, String body, @AuthenticationPrincipal AuthUser principal) {
        JSONObject o = new JSONObject(body);

        return MessageManager.message(
                id,
                channelId,
                principal.getAccount().getId(),
                o.has("reply")?o.getLong("reply"):-1,
                o.getString("content")
        ) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/server/{id}/{channelId}/{messageId}")
    public ResponseEntity<?> editMessage(@PathVariable Long id, @PathVariable Long channelId, @PathVariable Long messageId, String body, @AuthenticationPrincipal AuthUser principal) {
        JSONObject o = new JSONObject(body);

        return MessageManager.edit(
                id,
                channelId,
                messageId,
                principal.getAccount().getId(),
                o.getString("content")
        ) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/server/{id}/{channelId}/{messageId}")
    public ResponseEntity<?> editMessage(@PathVariable Long id, @PathVariable Long channelId, @PathVariable Long messageId, @AuthenticationPrincipal AuthUser principal) {
        return MessageManager.delete(
                id,
                channelId,
                messageId,
                principal.getAccount().getId()
        ) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
