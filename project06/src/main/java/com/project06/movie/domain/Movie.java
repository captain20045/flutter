package com.project06.movie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Map;

@Entity
@Table(name="movie")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "overview")
    private String overview; // 영화 설명
    
    @Column(name = "poster_path")
    private String poster_path;
    
    @Column(name = "release_date")
    private String release_date;
    
    @Column(name = "vote_average")
    private Double voteAverage;
    
    @Column(name = "adult")
    private Boolean adult; 
    
    private Double recencyScore; // 추가
    private Double tfidfScore; // 추가
    
    @Transient // 장르 ID를 저장하지 않고, 임시로 사용
    private List<Integer> genreIds = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>(); // 영화 장르 이름 저장

    public Movie() {
        // Default constructor for JPA and Jackson
    }
    
    public Movie(Long id, String title, String overview, String poster_path, String release_date, Double voteAverage, Boolean adult, Set<String> genres) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.voteAverage = voteAverage;
        this.adult = adult;
        this.genres = genres;
    }
    
    public void addGenre(String genreName) {
        this.genres.add(genreName);
    }
    
    public Set<String> getGenres() {
		return genres;
	}

	public void setGenres(Set<String> genres) {
		this.genres = genres;
	}

	public void setGenresNames(Map<Integer, String> genreMap, List<Integer> genreIds) {
        this.genres.clear();
        if (genreIds != null) {
            for (Integer genreId : genreIds) {
                String genre = genreMap.get(genreId);
                if (genre != null) {
                    this.genres.add(genre);
                }
            }
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	public Double getVote_average() {
		return voteAverage;
	}

	public void setVote_average(Double voteAverage) {
		this.voteAverage = voteAverage;
	}

	public Boolean getAdult() {
		return adult;
	}

	public void setAdult(Boolean adult) {
		this.adult = adult;
	}
    
    @JsonProperty("genre_ids")
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    @JsonProperty("genre_ids")
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = (genreIds == null) ? new ArrayList<>() : genreIds; // null이 입력되면 빈 리스트 할당
    }
    
}
