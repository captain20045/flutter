package com.project06.viewing_history.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project06.viewing_history.domain.ViewingHistory;
import com.project06.viewing_history.repository.ViewingHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ViewingHistoryService {

    @Autowired
    private ViewingHistoryRepository viewingHistoryRepository;

    @Transactional
    public void saveViewingHistory(ViewingHistory viewingHistory) {
        // 시청 날짜를 오늘 날짜로 설정할 수 있습니다.
        viewingHistory.setViewingDate(LocalDate.now());
        viewingHistoryRepository.save(viewingHistory);
    }
    
    public List<ViewingHistory> getViewedMovies(String userId) {
        return viewingHistoryRepository.findByUserId(userId);
    }
}
