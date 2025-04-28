package com.event_management.security;

import com.event_management.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        // Get access token to call /user/emails endpoint
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String Authprovider = userRequest.getClientRegistration().getRegistrationId();
        String email = null;

        if ("github".equals(Authprovider)) {
            email = fetchGithubEmail(userRequest.getAccessToken().getTokenValue());
        } else {
            email = user.getAttribute("email");
        }

        // Check if the user already exists in the database
        if (userService.isUserExists(email)) {
        } else {
            userService.createUserwithOAuth(user);
        }

        return createOAuth2User(userRequest, user, email);
    }

    // Merge the email into attributes
    public OAuth2User createOAuth2User(
            OAuth2UserRequest userRequest, OAuth2User user, String email) {
        Map<String, Object> attributes = new HashMap<>(user.getAttributes());
        attributes.put("email", email);

        String userNameAttributeName =
                userRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        return new DefaultOAuth2User(user.getAuthorities(), attributes, userNameAttributeName);
    }

    public String fetchGithubEmail(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);
        headers.add("Accept", "application/vnd.github.v3+json");
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        // Call the GitHub API to get the user's email only when the user is actually trying to log
        // in via Giyhub bcz google gives the gmail
        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(
                        "https://api.github.com/user/emails",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<>() {});

        String email = null;
        for (Map<String, Object> emailEntry : response.getBody()) {
            if (Boolean.TRUE.equals(emailEntry.get("primary"))
                    && Boolean.TRUE.equals(emailEntry.get("verified"))) {
                email = (String) emailEntry.get("email");
                break;
            }
        }
        return email;
    }
}
