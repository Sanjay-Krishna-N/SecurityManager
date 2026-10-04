package org.secured.service;

import org.secured.model.request.LoginRequest;
import org.secured.model.request.RefreshRequest;
import org.secured.model.request.RegisterRequest;
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
public interface JwtService {
    ResponseEntity<?> doLogin(LoginRequest request);
    ResponseEntity<?> doRegister(RegisterRequest request);
    ResponseEntity<?> doRefresh(RefreshRequest request);
    ResponseEntity<?> doLogout(RefreshRequest request);
}
