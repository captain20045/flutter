package com.project06.movie.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.project06.movie.domain.ApiResponse;
import com.project06.movie.domain.Movie;
import com.project06.movie.domain.MovieGenres;
import com.project06.movie.domain.Genre;
import com.project06.movie.repository.MovieGenresRepository;
import com.project06.movie.repository.MovieRepository;
import com.project06.viewing_history.domain.ViewingHistory;
import com.project06.viewing_history.repository.ViewingHistoryRepository;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ViewingHistoryRepository viewingHistoryRepository;
    @Autowired
    private MovieGenresRepository movieGenresRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String apiKey = "92a5f04ab68edcae1efff0457834c673";
    private final String genreUrl = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + apiKey + "&language=en-US";
    private final String moviesUrl = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=en-US&page=";

    @Transactional
    public void importMovies() {
        if (movieRepository.count() >= 95) {
            System.out.println("Database already has 95 or more movies, skipping data import.");
            return;
        }

        try {
            Map<Integer, String> genreMap = fetchGenres(new RestTemplate(), new ObjectMapper());
            importMovieData(new RestTemplate(), new ObjectMapper(), genreMap);
        } catch (RestClientException e) {
            System.out.println("An error occurred while fetching data from the external API: " + e.getMessage());
        }
    }

    private void importMovieData(RestTemplate restTemplate, ObjectMapper mapper, Map<Integer, String> genreMap) {
        int totalMoviesToFetch = 100;
        int pageSize = 20;
        int totalPages = totalMoviesToFetch / pageSize;

        for (int page = 1; page <= totalPages; page++) {
            String url = moviesUrl + page;
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                ApiResponse apiResponse = mapper.readValue(response.getBody(), ApiResponse.class);
                List<Movie> movies = apiResponse.getResults();
                for (Movie movie : movies) {
                    for (Integer genreId : movie.getGenreIds()) {
                        String genre = genreMap.get(genreId);
                        if (genre != null) {
                            movie.addGenre(genre);
                        }
                    }
                    movieRepository.saveAll(movies);
                }
            } catch (Exception e) {
                System.out.println("Error fetching movies from page " + page + ": " + e.getMessage());
            }
        }
    }

    private Map<Integer, String> fetchGenres(RestTemplate restTemplate, ObjectMapper mapper) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(genreUrl, String.class);
            Map<String, List<Genre>> genresResponse = mapper.readValue(response.getBody(), new TypeReference<Map<String, List<Genre>>>() {});
            Map<Integer, String> genreMap = new HashMap<>();
            for (Genre genre : genresResponse.get("genres")) {
                genreMap.put(genre.getId(), genre.getName());
            }
            return genreMap;
        } catch (Exception e) {
            System.out.println("Error fetching genre data: " + e.getMessage());
            return new HashMap<>();
        }
    }

    @Transactional
    public Page<Movie> getRecommendedMovies(String userId, Pageable pageable) {
        // Step 1: Calculate content-based recommendations using TF-IDF
        Set<Long> tfidfRecommendedMovieIds = getTFIDFRecommendedMovies(userId);

        // Step 2: Fetch the final recommended movies from the repository
        List<Movie> recommendedMovies = movieRepository.findAllById(tfidfRecommendedMovieIds);
        return new PageImpl<>(recommendedMovies, pageable, recommendedMovies.size());
    }

    private Set<Long> getTFIDFRecommendedMovies(String userId) {
        String sql = """
            SELECT
                m.id,
                m.title,
                m.poster_path,
                m.overview, 
                SUM(DATEDIFF(CURDATE(), vh.viewing_date)) AS recency_score,
                (COUNT(*) / total.total_count) * LOG10(user_count.total_user_count / genre_count.genre_count) AS tfidf
            FROM
                viewing_history vh
            JOIN
                movie_genres mg ON vh.movie_id = mg.movie_id
            JOIN
                movie m ON m.id = vh.movie_id
            JOIN
                (SELECT userid, COUNT(*) AS total_count FROM viewing_history WHERE userid = ? GROUP BY userid) total ON vh.userid = total.userid
            JOIN
                (SELECT genre, COUNT(DISTINCT userid) AS genre_count FROM viewing_history vh JOIN movie_genres mg ON vh.movie_id = mg.movie_id GROUP BY genre) genre_count ON mg.genre = genre_count.genre
            CROSS JOIN
                (SELECT COUNT(DISTINCT userid) AS total_user_count FROM viewing_history) user_count
            WHERE
                vh.userid = ?
            GROUP BY
                m.id, m.title, m.poster_path, m.overview, total.total_count, genre_count.genre_count, user_count.total_user_count
            ORDER BY
                tfidf DESC, recency_score ASC;
        """;

        Object[] params = new Object[] {userId, userId};
        List<Long> movieIds = jdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("id"));
        return new HashSet<>(movieIds);
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Page<Movie> searchMovies(String title, Pageable pageable) {
        return movieRepository.findByTitleContaining(title, pageable);
    }
}
