package net.yellowstrawberry.pcospot.controller;

import net.yellowstrawberry.pcospot.object.recruitment.Recruitment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentController {

    private final OkHttpClient client = new OkHttpClient();
    @Value("${pcospot.ai.endpoint}")
    private String pcospotAiEndpoint;

    @PostMapping("/")
    public String createRecruitment(String body) {
        JSONObject o = new JSONObject(body);
        Recruitment r = new Recruitment(o.getString("title"), o.getString("description"), new Date(o.getLong("startDate")), new Date(o.getLong("endDate")), toStringList(o.getJSONArray("roles")), o.getString("requirements"));
        return "{\"id\": %d}".formatted(r.getRecruitmentId());
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String q, @RequestParam(required = false) Integer limit) {
        Request request = new Request.Builder()
                .url(pcospotAiEndpoint+"/search?q="+ URLEncoder.encode(q, StandardCharsets.UTF_8)+"&limit="+limit)
                .build();

        try (Response res = client.newCall(request).execute()) {
            return new ResponseEntity<>(res.body().string(), HttpStatusCode.valueOf(res.code()));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    private static List<String> toStringList(JSONArray array) {
        List<String> l= new ArrayList<>();
        for(int i=0; i<array.length(); i++) {
            l.add(array.optString(i));
        }
        return l;
    }
}
