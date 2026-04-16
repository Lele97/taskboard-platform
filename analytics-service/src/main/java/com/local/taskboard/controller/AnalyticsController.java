package com.local.taskboard.controller;

import com.local.taskboard.entity.BoardAnalytics;
import com.local.taskboard.service.AnalitycsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@AllArgsConstructor
public class AnalyticsController {

    private final AnalitycsService analitycsService;

    @GetMapping()
    public ResponseEntity<?> getAnalytics() {
        try {
            List<BoardAnalytics> listboardAnalytics = analitycsService.getBoardAnalytics();
            return new ResponseEntity<>(listboardAnalytics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getAnalyticsByBoardId(@PathVariable String boardId) {
        try {
            BoardAnalytics boardAnalytics = analitycsService.getBoardAnalyticsById(boardId);
            return new ResponseEntity<>(boardAnalytics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
