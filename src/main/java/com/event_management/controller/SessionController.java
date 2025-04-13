package com.event_management.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

import com.event_management.service.SessionValidationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    
    @Autowired
    private SessionValidationService sessionValidationService;
    
    /**
     * Endpoint to validate a session token
     * @param request The HTTP request containing the session token
     * @return SessionStatus indicating if the session is valid
     */
    @GetMapping("/validate")
    public ResponseEntity<SessionStatus> validateSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION-TOKEN".equals(cookie.getName())) {
                    boolean isValid = sessionValidationService.validateSessionToken(cookie.getValue());
                    return ResponseEntity.ok(new SessionStatus(isValid));
                }
            }
        }
        
        // No session token found
        return ResponseEntity.ok(new SessionStatus(false));
    }
    
    /**
     * Simple DTO for session status
     */
    public static class SessionStatus {
        private final boolean valid;
        
        public SessionStatus(boolean valid) {
            this.valid = valid;
        }
        
        public boolean isValid() {
            return valid;
        }
    }
    
    /**
     * Another endpoint that clients can use to check if their session is still active
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkSession(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        if (authentication != null && authentication.isAuthenticated()) {
            response.put("authenticated", true);
            response.put("username", authentication.getName());
            
            // Add basic user information
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                response.put("authorities", userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            }
        } else {
            response.put("authenticated", false);
        }
        
        return ResponseEntity.ok(response);
    }
}
