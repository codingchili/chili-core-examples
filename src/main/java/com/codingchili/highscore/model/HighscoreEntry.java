package com.codingchili.highscore.model;

/**
 * @author Robin Duda
 *
 * Model class used to store a single highscore record.
 */
public class HighscoreEntry {
    public String player;
    public Integer score;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof HighscoreEntry && ((HighscoreEntry) obj).player.equals(player));
    }
}
