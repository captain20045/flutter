package com.project06.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project06.movie.domain.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    long count(); // 저장된 영화의 총 개수를 반환

    Page<Movie> findAll(Pageable pageable);
    Page<Movie> findByTitleContaining(String title, Pageable pageable);
    Page<Movie> findByOrderByVoteAverageDesc(Pageable pageable);

    // 새로운 쿼리 메소드: 장르에 따른 영화 검색
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g = :genre")
    Page<Movie> findByGenre(@Param("genre") String genre, Pageable pageable);

}
