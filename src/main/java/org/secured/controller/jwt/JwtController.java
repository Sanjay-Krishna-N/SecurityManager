package org.secured.controller.jwt;

import lombok.RequiredArgsConstructor;
import org.secured.util.jwt.JwtUtil;
import org.secured.util.jwt.RefreshTokenStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/jwt")
@RequiredArgsConstructor
public class JwtController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenStore refreshTokenStore;

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "password123";

    @PostMapping("v1/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (!VALID_USERNAME.equals(request.username()) || !VALID_PASSWORD.equals(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(request.username());
        String refreshToken = jwtUtil.generateRefreshToken(request.username());

        refreshTokenStore.store(refreshToken, request.username());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("v1/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        String token = request.refreshToken();

        if (!jwtUtil.isTokenValid(token) || !"refresh".equals(jwtUtil.extractType(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        if (!refreshTokenStore.isValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token revoked or unknown");
        }

        String username = jwtUtil.extractUsername(token);

        // rotate: invalidate old refresh token, issue a new pair
        refreshTokenStore.revoke(token);
        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);
        refreshTokenStore.store(newRefreshToken, username);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
    }

    @PostMapping("v1/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest request) {
        refreshTokenStore.revoke(request.refreshToken());
        return ResponseEntity.ok("Logged out");
    }
}

record LoginRequest(String username, String password) {}
record RefreshRequest(String refreshToken) {}
record AuthResponse(String accessToken, String refreshToken) {}