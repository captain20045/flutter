package com.project06.member.controller;

import com.project06.member.domain.Member;
import com.project06.member.service.MemberService;
import com.project06.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;


@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService userService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/registerAction")
    public ResponseEntity<?> registerUser(@RequestBody Member user) {
        Member savedUser = userService.addUser(user);
        return ResponseEntity.ok().body(Map.of("success", savedUser != null));
    }
    
    @PostMapping("/loginAction")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String userid = credentials.get("userid");
        String userpw = credentials.get("userpw");

        if (userService.validateUserLogin(userid, userpw)) {
            String token = jwtTokenProvider.generateToken(userid);
            Map<String, String> response = new HashMap<>();
            response.put("username", userid);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Logged out successfully."));
    }

    @GetMapping("/user/details/{userid}")
    public ResponseEntity<?> getUserDetails(@PathVariable("userid") String userid) {
        Optional<Member> user = userService.findUserById(userid);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody Member user) {
        boolean isUpdated = userService.updateUser(user);
        if (isUpdated) {
            return ResponseEntity.ok().body(Map.of("message", "User updated successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Update failed"));
        }
    }

    @DeleteMapping("/user/delete/{userid}")
    public ResponseEntity<?> deleteUser(@PathVariable String userid) {
        boolean isDeleted = userService.deleteUser(userid);
        if (isDeleted) {
            return ResponseEntity.ok().body(Map.of("message", "User deleted successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Deletion failed"));
        }
    }

    // Additional endpoints can be added here as needed
}
