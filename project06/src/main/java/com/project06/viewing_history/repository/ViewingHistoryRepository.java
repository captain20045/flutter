package com.project06.viewing_history.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project06.viewing_history.domain.ViewingHistory;

@Repository
public interface ViewingHistoryRepository extends JpaRepository<ViewingHistory, Long> {
	List<ViewingHistory> findByUserId(String userId);
	List<ViewingHistory> findByMovieIdIn(Set<Long> movieIds);
	
}
