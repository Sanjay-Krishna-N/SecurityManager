package org.secured.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.secured.configs.bcrypt.BcryptService;
import org.secured.model.request.RefreshRequest;
import org.secured.model.request.AuthResponse;
import org.secured.model.request.LoginRequest;
import org.secured.model.request.RegisterRequest;
import org.secured.repository.UsersRepository;
import org.secured.service.JwtService;
import org.secured.util.jwt.JwtUtil;
import org.secured.util.jwt.RefreshTokenStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * ------------------------------------------------------------------------
 * Author   : Sanjay Krishna Narayanan
 * Created  : 10/5/26
 * Version  : 1.0
 * ------------------------------------------------------------------------
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final UsersRepository usersRepository;
    private final BcryptService bcryptService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenStore refreshTokenStore;

    @NonNull
    public ResponseEntity<?> doLogin(LoginRequest request) {
        RegisterRequest registerRequest = usersRepository.findByUsername(request.username());
        log.info(registerRequest.toString());
        if (!registerRequest.getUsername().equals(request.username()) || !bcryptService.checkPassword(request.password(), registerRequest.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } else {
            log.info("User logged in successfully");
        }
        String accessToken = jwtUtil.generateAccessToken(request.username());
        String refreshToken = jwtUtil.generateRefreshToken(request.username());
        refreshTokenStore.store(refreshToken, request.username());
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }
    @NonNull
    public ResponseEntity<String> doRegister(RegisterRequest registerRequest) {
        if(usersRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username taken");
        }
        registerRequest.setPasswordHash(bcryptService.getBCryptPasswordEncoder(registerRequest.getPasswordHash()));
        usersRepository.save(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
    @NonNull
    public ResponseEntity<?> doRefresh(RefreshRequest request) {
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
    @NonNull
    public ResponseEntity<String> doLogout(RefreshRequest request) {
        refreshTokenStore.revoke(request.refreshToken());
        return ResponseEntity.ok("Logged out");
    }
}
