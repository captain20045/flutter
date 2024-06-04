package com.project06.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project06.movie.domain.Movie;
import com.project06.movie.service.MovieService;



@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Movie>> searchMovies(@RequestParam String query, Pageable pageable) {
        Page<Movie> movies = movieService.searchMovies(query, pageable);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestParam String userId, Pageable pageable) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        Page<Movie> movies = movieService.getRecommendedMovies(userId, pageable);
        return ResponseEntity.ok(movies);
    }
}
