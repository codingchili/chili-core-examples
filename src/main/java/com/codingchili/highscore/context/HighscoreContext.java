package com.codingchili.highscore.context;

import com.codingchili.core.configuration.CoreStrings;
import com.codingchili.core.context.CoreContext;
import com.codingchili.core.context.SystemContext;
import com.codingchili.core.files.Configurations;


/**
 * Contexts provides access to the configuration of the running service.
 */
public class HighscoreContext extends SystemContext {
    private static final String STATS = "highscore";

    public HighscoreContext(CoreContext core) {
        super(core);
    }

    /**
     * Returns the currently loaded settings - will be refreshed in Configurations
     * when the file is modified.
     *
     * @return highscore settings from file if exists, or defaults from class.
     */
    public HighscoreSettings settings() {
        return Configurations.get(CoreStrings.getService(STATS), HighscoreSettings.class);
    }
}
