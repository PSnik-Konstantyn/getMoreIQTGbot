package com.iq;

import java.util.Date;

public class PlayerInfo {
    private Long playerId;
    private Long chatId;

    public Integer getAllAnswered() {
        return allAnswers;
    }

    public void setAllAnswers(Integer allAnswers) {
        this.allAnswers = allAnswers;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    private Integer allAnswers;

    private Integer correctAnswers;

    public Integer getAttemptCounter() {
        return attemptCounter;
    }

    public void setAttemptCounter(Integer attemptCounter) {
        this.attemptCounter = attemptCounter;
    }

    private Integer attemptCounter;

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    private String userFirstName;
    private int iqCounter;

    public boolean isCanAnswer() {
        return canAnswer;
    }

    public void setCanAnswer(boolean canAnswer) {
        this.canAnswer = canAnswer;
    }

    private boolean canAnswer;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public int getIqCounter() {
        return iqCounter;
    }

    public void setIqCounter(int iqCounter) {
        this.iqCounter = iqCounter;
    }

    private Date lastTimePlayed;

    public Date getLastTimePlayed() {
        return lastTimePlayed;
    }

    public void setLastTimePlayed(Date lastTimePlayed) {
        this.lastTimePlayed = lastTimePlayed;
    }
}
