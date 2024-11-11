package net.yellowstrawberry.pcospot.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2Service extends DefaultOAuth2UserService {
//    @Autowired
//    private AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        //TODO
        return null;
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
//
//        Account account = accountRepository.findByNvid((String) response.get("id")).orElse(null);
//        if(account==null) {
//            account = new Account(4, (String) response.get("name"), (String) response.get("email"), (String) response.get("id"));
//            account = accountRepository.save(account);
//        }
//
//        Object d = null;
//        if(account.getRole()==2) {
//            d = teacherRepository.findById(account.getId()).orElse(null);
//        }else if(account.getRole()==3) {
//            d = studentRepository.findById(account.getId()).orElse(null);
//        }
//
//        return new AuthUser(account, d, oAuth2User);
    }
}
