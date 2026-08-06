package com.event_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class SessionValidationService {

    @Autowired private SessionRegistry sessionRegistry;

    /**
     * Validates if a session token is valid
     *
     * @param token The session token to validate
     * @return true if valid, false otherwise
     */
    public boolean validateSessionToken(String token) {
        try {
            // Decode the token
            String decodedToken = new String(Base64.getDecoder().decode(token));
            String[] parts = decodedToken.split(":");

            if (parts.length != 2) {
                return false;
            }

            String username = parts[0];
            long timestamp = Long.parseLong(parts[1]);

            // Check if token is expired (24 hour validity in this example)
            if (System.currentTimeMillis() - timestamp > 24 * 60 * 60 * 1000) {
                return false;
            }

            // Check if user is still logged in
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(username, false);
            return !sessions.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
