package com.project06.movie.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project06.movie.service.MovieService;

@Component
public class MovieDataLoader implements CommandLineRunner {

    private final MovieService movieService;

    public MovieDataLoader(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void run(String... args) throws Exception {
        movieService.importMovies();
    }
}