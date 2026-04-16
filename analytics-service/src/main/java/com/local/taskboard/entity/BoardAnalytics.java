package com.local.taskboard.entity;

import lombok.Data;

import java.util.List;

@Data
public class BoardAnalytics {

    private String boardId;
    private String boardName;
    private int totalCards;
    private int todoCount;
    private int inProgressCount;
    private int doneCount;
    private double completionRate; // percentuale card DONE / total
    private List<AssigneeStats> assigneeStats; // opzionali
}
