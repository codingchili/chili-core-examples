package com.codingchili.highscore.context;

import com.codingchili.core.configuration.Configurable;

/**
 * @author Robin Duda
 *
 * Settings file that is read from the /conf/service/stats.json file.
 */
public class HighscoreSettings implements Configurable {
    private int maxCount = 8;

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public String getPath() {
        return "highscore.json";
    }
}