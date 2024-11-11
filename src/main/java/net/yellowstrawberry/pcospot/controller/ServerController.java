package net.yellowstrawberry.pcospot.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/server/")
public class ServerController {


    @GetMapping("/{id}/")
    public void info(@AuthenticationPrincipal OAuth2User principal) {
        
    }
}
