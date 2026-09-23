package org.secured.app.securitymanager;

/**
 * ------------------------------------------------------------------------
 * Author   : Sanjay Krishna Narayanan
 * Created  : 9/23/26
 * Version  : 1.0
 * ------------------------------------------------------------------------
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.secured")
public class SecurityManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityManagerApplication.class, args);
    }

}
