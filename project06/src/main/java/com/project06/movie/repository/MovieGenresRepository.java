package com.project06.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project06.movie.domain.MovieGenres;

import java.util.List;
import java.util.Set;

public interface MovieGenresRepository extends JpaRepository<MovieGenres, Long> {
    List<MovieGenres> findByGenre(String genre);
    List<MovieGenres> findByMovieId(Long movieId);
    List<MovieGenres> findByMovieIdIn(List<Long> movieIds);
    List<MovieGenres> findByGenreIn(Set<String> genres);
}
