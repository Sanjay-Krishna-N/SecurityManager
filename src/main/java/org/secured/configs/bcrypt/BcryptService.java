package org.secured.configs.bcrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * ------------------------------------------------------------------------
 * Author   : Sanjay Krishna Narayanan
 * Created  : 9/28/26
 * Version  : 1.0
 * ------------------------------------------------------------------------
 */
@Component
public class BcryptService {

    @Autowired
    private Bcrypt bcrypt;

    public String getBCryptPasswordEncoder(String rawPassword) {
        return bcrypt.passwordEncoder().encode(rawPassword);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return bcrypt.passwordEncoder().matches(rawPassword, encodedPassword);
    }
}
