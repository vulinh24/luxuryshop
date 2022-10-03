package com.luxuryshop.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.luxuryshop.entities.Role;
import com.luxuryshop.entities.RoleOfUser;
import com.luxuryshop.entities.User;
import com.luxuryshop.entities.primarykey.PKOfRoleUser;
import com.luxuryshop.model.GoogleUser;
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
public class LoginGGService {
    @Autowired
    OkHttpService okHttpService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final String GOOGLE_APP_ID = "652114865882-gr3tn964j0vhar926d74qsg7aoq5c25n.apps.googleusercontent.com";
    private final String GOOGLE_APP_SECRET = "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";
    private final String GOOGLE_REDIRECT_URL = "http://localhost:8888/auth/google-login";
    private final String GOOGLE_LINK_GET_TOKEN = "https://www.googleapis.com/oauth2/v4/token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s&grant_type=authorization_code&scope=openid email profile";
    private final String GOOGLE_LINK_GET_INFO = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=%s";

    public String getToken(final String code) {
        String link = String.format(GOOGLE_LINK_GET_TOKEN, GOOGLE_APP_ID, GOOGLE_APP_SECRET, GOOGLE_REDIRECT_URL, code);
        String response = null;
        try {
            response = okHttpService.post(link, null);
            JsonNode node = mapper.readTree(response).get("access_token");
            return node.textValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GoogleUser getUser(final String accessToken) {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String link = String.format(GOOGLE_LINK_GET_INFO, accessToken);
        String response = null;
        try {
            response = okHttpService.get(link);
            return mapper.readValue(response, GoogleUser.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loginByCode(String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = getToken(code);
        GoogleUser googleUser = getUser(token);
        User user = userRepository.findByUsername(googleUser.getSub());
        if (user == null) {
            User newUser = new User();
            newUser.setUsername(googleUser.getSub());
            newUser.setEmail(googleUser.getEmail());
            newUser.setName(googleUser.getName());
            newUser.setPassword("social-1");
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
