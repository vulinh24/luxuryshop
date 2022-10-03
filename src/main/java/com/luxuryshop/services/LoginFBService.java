package com.luxuryshop.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.luxuryshop.entities.Role;
import com.luxuryshop.entities.RoleOfUser;
import com.luxuryshop.entities.User;
import com.luxuryshop.entities.primarykey.PKOfRoleUser;
import com.luxuryshop.model.FacebookUser;
import com.luxuryshop.repositories.RoleRepository;
import com.luxuryshop.repositories.RoleUserRepository;
import com.luxuryshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class LoginFBService {

    @Autowired
    OkHttpService okHttpService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final String FACEBOOK_APP_ID = "5984266168268721";
    private final String FACEBOOK_APP_SECRET = "757d8168630e2eb5c33334c09a02fbad";
    private final String FACEBOOK_REDIRECT_URL = "http://localhost:8888/auth/facebook-login";
    private final String FACEBOOK_LINK_GET_TOKEN = "https://graph.facebook.com/oauth/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s";
    private final String FACEBOOK_LINK_GET_INFO = "https://graph.facebook.com/v10.0/me?fields=id,name,email,birthday,first_name,last_name,link,location,picture&access_token=%s";

    public String getToken(final String code) {
        String link = String.format(FACEBOOK_LINK_GET_TOKEN, FACEBOOK_APP_ID, FACEBOOK_APP_SECRET, FACEBOOK_REDIRECT_URL, code);
        String response = null;
        try {
            response = okHttpService.get(link);
            JsonNode node = mapper.readTree(response).get("access_token");
            return node.textValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FacebookUser getUser(final String accessToken) {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String link = String.format(FACEBOOK_LINK_GET_INFO, accessToken);
        String response = null;
        try {
            response = okHttpService.get(link);
            return mapper.readValue(response, FacebookUser.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loginByCode(String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = getToken(code);
        FacebookUser facebookUser = getUser(token);
        User user = userRepository.findByUsername(facebookUser.getId());
        if (user == null) {
            User newUser = new User();
            newUser.setUsername(facebookUser.getId());
            newUser.setEmail(facebookUser.getEmail());
            newUser.setName(facebookUser.getName());
            newUser.setPassword("social-0");
            newUser = userRepository.save(newUser);
            PKOfRoleUser pk = new PKOfRoleUser(2, newUser.getId());
            roleUserRepository.save(new RoleOfUser(pk));
            Role role = roleRepository.findByName("ROLE_MEMBER");
            newUser.setRoles(List.of(role));
            response(request, response, newUser);
        } else {
            response(request, response, user);
        }
    }

    private void response(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        MyUserDetail userDetail = new MyUserDetail(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute("USER", user.getUsername());
        request.getSession().setAttribute("memberName", user.getName());
        request.getSession().removeAttribute("gCART");
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
