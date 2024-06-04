package com.project06.viewing_history.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.project06.viewing_history.domain.ViewingHistory;
import com.project06.viewing_history.service.ViewingHistoryService;

import jakarta.servlet.http.HttpServletRequest;

import com.project06.security.JwtTokenProvider;

@RestController
@RequestMapping("/api")
public class ViewingHistoryController {

    @Autowired
    private ViewingHistoryService viewingHistoryService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/recordViewingHistory")
    public ResponseEntity<String> recordViewingHistory(@RequestBody ViewingHistory viewingHistory) {
        try {
            viewingHistoryService.saveViewingHistory(viewingHistory);
            return ResponseEntity.ok("Viewing history recorded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to record viewing history");
        }
    }
    
    @GetMapping("/viewedMovies/{userId}")
    public ResponseEntity<?> getViewedMoviesByUserId(@PathVariable String userId, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Unauthorized access or invalid token.\"}");
        }
 
        try {
            List<ViewingHistory> movies = viewingHistoryService.getViewedMovies(userId);
            if (movies.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"No viewing history found.\"}");
            }
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }
}
