package net.yellowstrawberry.pcospot.controller;

import net.yellowstrawberry.pcospot.object.recruitment.Recruitment;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentController {

    @PostMapping("/")
    public String createRecruitment(String body) {
        JSONObject o = new JSONObject(body);
        Recruitment r = new Recruitment(o.getString("title"), o.getString("description"), new Date(o.getLong("startDate")), new Date(o.getLong("endDate")), toStringList(o.getJSONArray("roles")), o.getString("requirements"));
        return "{\"id\": %d}".formatted(r.getRecruitmentId());
    }

    private static List<String> toStringList(JSONArray array) {
        List<String> l= new ArrayList<>();
        for(int i=0; i<array.length(); i++) {
            l.add(array.optString(i));
        }
        return l;
    }
}
