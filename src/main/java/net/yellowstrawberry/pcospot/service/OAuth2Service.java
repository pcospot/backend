package net.yellowstrawberry.pcospot.service;

import net.yellowstrawberry.pcospot.db.repository.UserRepository;
import net.yellowstrawberry.pcospot.object.user.AuthUser;
import net.yellowstrawberry.pcospot.object.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class OAuth2Service extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(req);
        Optional<User> oUser =  userRepository.findById(oAuth2User.getAttribute("sub"));

        if(oUser.isPresent()) {
            return new AuthUser(oUser.get(), oAuth2User);
        }else {
            User user = new User();
            user.setId(oAuth2User.getAttribute("sub"));
            user.setDescription("");
            user.setUsername(oAuth2User.getName());
            user.setNickname(oAuth2User.getAttribute("name"));
            user.setJoined(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            return new AuthUser(user, oAuth2User);
        }
    }
}
