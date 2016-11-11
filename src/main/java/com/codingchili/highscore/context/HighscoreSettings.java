package com.codingchili.highscore.context;

import com.codingchili.core.Configuration.ServiceConfigurable;

/**
 * @author Robin Duda
 *
 * Settings file that is read from the /conf/service/stats.json file.
 */
class HighscoreSettings extends ServiceConfigurable {
    int maxCount;

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}