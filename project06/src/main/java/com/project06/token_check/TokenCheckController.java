package com.project06.token_check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.project06.security.JwtTokenProvider;

@RestController
public class TokenCheckController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/verifyToken")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.ok().body("Token is valid");
            }
        }
        return ResponseEntity.status(401).body("Invalid or expired token");
    }
}
