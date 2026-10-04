package org.secured.controller.jwt;

/**
 * ------------------------------------------------------------------------
 * Author   : Sanjay Krishna Narayanan
 * Created  : 9/23/26
 * Version  : 1.0
 * ------------------------------------------------------------------------
 */

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.secured.configs.bcrypt.BcryptService;
import org.secured.model.request.LoginRequest;
import org.secured.model.request.RefreshRequest;
import org.secured.model.request.RegisterRequest;
import org.secured.repository.UsersRepository;
import org.secured.service.JwtService;
import org.secured.util.jwt.JwtUtil;
import org.secured.util.jwt.RefreshTokenStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/jwt")
@RequiredArgsConstructor
@Slf4j
public class JwtController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenStore refreshTokenStore;
    private final UsersRepository usersRepository;
    private final BcryptService bcryptService;
    private final JwtService jwtService;

    @PostMapping("v1/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return jwtService.doRegister(registerRequest);
    }

    @PostMapping("v1/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return jwtService.doLogin(request);
    }

    @PostMapping("v1/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        return jwtService.doRefresh(request);
    }

    @PostMapping("v1/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest request) {
        return jwtService.doLogout(request);
    }
}

