package com.project06.viewing_history.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "viewing_history")
public class ViewingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userid")
    private String userId;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "viewing_date")
    private LocalDate viewingDate;

    // 기본 생성자
    public ViewingHistory() {
    	
    }

    // 모든 필드를 포함하는 생성자
    public ViewingHistory(String userId, Long movieId, LocalDate viewingDate) {
        this.userId = userId;
        this.movieId = movieId;
        this.viewingDate = viewingDate;
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public LocalDate getViewingDate() {
        return viewingDate;
    }

    public void setViewingDate(LocalDate viewingDate) {
        this.viewingDate = viewingDate;
    }
}
