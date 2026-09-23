package org.secured.util.jwt;

/**
 * ------------------------------------------------------------------------
 * Author   : Sanjay Krishna Narayanan
 * Created  : 9/23/26
 * Version  : 1.0
 * ------------------------------------------------------------------------
 */

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshTokenStore {

    private final Map<String, String> validTokens = new ConcurrentHashMap<>();

    public void store(String token, String username) {
        validTokens.put(token, username);
    }

    public boolean isValid(String token) {
        return validTokens.containsKey(token);
    }

    public void revoke(String token) {
        validTokens.remove(token);
    }

    public void revokeAllForUser(String username) {
        validTokens.values().removeIf(u -> u.equals(username));
    }
}