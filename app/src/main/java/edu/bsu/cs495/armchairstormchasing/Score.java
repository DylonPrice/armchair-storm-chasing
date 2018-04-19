package edu.bsu.cs495.armchairstormchasing;

public class Score {
    private int currentDayScore;
    private int totalScore;

    public Score(int currentDayScore, int totalScore) {
        this.currentDayScore = currentDayScore;
        this.totalScore = totalScore;
    }

    public int getCurrentDayScore() {
        return currentDayScore;
    }

    public void setCurrentDayScore(int currentDayScore) {
        this.currentDayScore = currentDayScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
