package com.project06.movie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private int page;
    private List<Movie> results;
    private int totalResults;
    private int totalPages;

    // getters and setters

    @JsonProperty("results")
    public List<Movie> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Movie> results) {
        this.results = results;
    }

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
    
}
