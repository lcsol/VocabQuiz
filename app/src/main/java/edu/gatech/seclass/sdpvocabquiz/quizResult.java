package edu.gatech.seclass.sdpvocabquiz;

import java.util.Date;

public class quizResult {
    private int id;
    private String quizName;
    private String playerName;
    private Double percentage;
    private String finishTime;

    public quizResult(String quizName, String playerName, Double percentage, String finishTime) {
        this.quizName = quizName;
        this.playerName = playerName;
        this.percentage = percentage;
        this.finishTime = finishTime;
    }

    public quizResult(int id, String quizName, String playerName, Double percentage, String finishTime) {
        this.id = id;
        this.quizName = quizName;
        this.playerName = playerName;
        this.percentage = percentage;
        this.finishTime = finishTime;
    }

    public int getId() { return id; }

    public String getQuizName() { return quizName; }

    public String getPlayerName() { return playerName; }

    public Double getPercentage() { return percentage; }

    public String getFinishTime() { return finishTime; }

    public void setId(int id) { this.id = id; }

}
