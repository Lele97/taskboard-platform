package com.local.taskboard.service;

import com.local.taskboard.entity.BoardAnalytics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class AnalitycsService {

    private final MongoTemplate mongoTemplate;

    public BoardAnalytics getBoardAnalyticsById(String boardId) throws Exception {

        if(mongoTemplate.count(
                new org.springframework.data.mongodb.core.query.Query(
                        Criteria.where("boardId").is(new org.bson.BsonString(boardId))
                ), "cards")==0){
            log.warn("La board con id: {} non esiste", boardId);
            throw new Exception("La board con id: " + boardId + " non esiste");
        }

        MatchOperation matchBoard = Aggregation.match(Criteria.where("boardId").is(boardId));

        GroupOperation groupByColumn = Aggregation.group("column")
                .count().as("count")
                .first("boardId").as("boardId");

        Document lookupStage = new Document("$lookup", new Document()
                .append("from", "board")
                .append("let", new Document("boardIdStr", "$boardId"))
                .append("pipeline", List.of(
                        new Document("$match", new Document("$expr",
                                new Document("$eq", List.of("$_id",new Document("$toObjectId", "$$boardIdStr")))
                        )),
                        new Document("$project", new Document("name", 1))
                ))
                .append("as", "boardInfo")
        );

        // Stage 4: flatten boardInfo array → boardName field
        Document addFieldsStage = new Document("$addFields",
                new Document("boardName",
                        new Document("$arrayElemAt", List.of("$boardInfo.name", 0))
                )
        );

        Aggregation aggregation = Aggregation.newAggregation(matchBoard, groupByColumn , context ->  lookupStage, context -> addFieldsStage);

        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation, "cards", Document.class
        );

        List<Document> columnStats = results.getMappedResults();

        BoardAnalytics analytics = new BoardAnalytics();
        analytics.setBoardId(boardId);
        analytics.setBoardName(results.getMappedResults().get(0).getString("boardName"));


        // ✅ Fix 1: usa Number per gestire sia Integer che Long
        analytics.setTotalCards(columnStats.stream()
                .mapToInt(d -> toInt(d.get("count")))
                .sum());

        columnStats.forEach(stat -> {
            String column = stat.getString("_id");

            int count = toInt(stat.get("count"));

            if(column != null){
                switch (column) {
                    case "TODO"        -> analytics.setTodoCount(count);
                    case "IN_PROGRESS" -> analytics.setInProgressCount(count);
                    case "DONE"        -> analytics.setDoneCount(count);
                }
            }

        });

        analytics.setCompletionRate(
                analytics.getTotalCards() > 0
                        ? (double) analytics.getDoneCount() / analytics.getTotalCards() * 100
                        : 0
        );

        return analytics;
    }

    // ✅ Helper condiviso per conversione numerica sicura
    private int toInt(Object value) {
        return value instanceof Number n ? n.intValue() : 0;
    }

    public List<BoardAnalytics> getBoardAnalytics() throws Exception {


    if(mongoTemplate.count(new org.springframework.data.mongodb.core.query.Query(),"cards")==0){
        log.warn("Non esistono cards");
        throw new Exception("Non esistono cards");
    }

    // Stage 1: group by compound key {boardId, column}
    GroupOperation groupByBoardAndColumn = Aggregation.group("boardId", "column")
            .count().as("count");

        Document lookupStage = new Document("$lookup", new Document()
                .append("from", "board")
                .append("let", new Document("boardIdStr", "$_id.boardId"))
                .append("pipeline", List.of(
                        new Document("$match", new Document("$expr",
                                new Document("$eq", List.of("$_id",new Document("$toObjectId", "$$boardIdStr")))
                        )),
                        new Document("$project", new Document("name", 1))
                ))
                .append("as", "boardInfo")
        );

        // Stage 4: flatten boardInfo array → boardName field
        Document addFieldsStage = new Document("$addFields",
                new Document("boardName",
                        new Document("$arrayElemAt", List.of("$boardInfo.name", 0))
                )
        );

    Aggregation aggregation = Aggregation.newAggregation(groupByBoardAndColumn, context ->  lookupStage, context -> addFieldsStage);

    AggregationResults<Document> results = mongoTemplate.aggregate(
            aggregation, "cards", Document.class
    );

    // Stage 2: build per-board analytics in Java
    Map<String, BoardAnalytics> analyticsMap = new LinkedHashMap<>();

    results.getMappedResults().forEach(doc -> {
        // compound _id is a nested Document: { boardId: "...", column: "..." }
        Document id = (Document) doc.get("_id");
        if (id == null) return;

        String boardId = id.getString("boardId");
        String column  = id.getString("column");
        String boardName = doc.getString("boardName"); // ✅ aggiungi questa riga
        int count      = toInt(doc.get("count"));

        if (boardId == null) return;


        BoardAnalytics analytics = analyticsMap.computeIfAbsent(boardId, k -> {
            BoardAnalytics a = new BoardAnalytics();
            a.setBoardId(k);
            a.setTotalCards(0);
            return a;
        });

        if (boardName != null) analytics.setBoardName(boardName); // ✅ setta il nome


        analytics.setTotalCards(analytics.getTotalCards() + count);

        if (column != null) {
            switch (column) {
                case "TODO"        -> analytics.setTodoCount(count);
                case "IN_PROGRESS" -> analytics.setInProgressCount(count);
                case "DONE"        -> analytics.setDoneCount(count);
            }
        }
    });

    // Compute completion rate per board
    analyticsMap.values().forEach(a ->
            a.setCompletionRate(
            a.getTotalCards() > 0
            ? (double) a.getDoneCount() / a.getTotalCards() * 100
            : 0
            )
            );

    return new ArrayList<>(analyticsMap.values());
}
}