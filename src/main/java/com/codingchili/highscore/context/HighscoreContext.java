package com.codingchili.highscore.context;

import com.codingchili.core.configuration.CoreStrings;
import com.codingchili.core.context.CoreContext;
import com.codingchili.core.context.SystemContext;
import com.codingchili.core.files.Configurations;


/**
 * Contexts provides access to the configuration of the running service. It is
 * recommended to leave service() protected and instead provide helper methods to
 * avoid the result of Configurations.get being cached.
 */
public class HighscoreContext extends SystemContext {
    private static final String STATS = "highscore";

    public HighscoreContext(CoreContext core) {
        super(core);
    }

    public HighscoreSettings settings() {
        return Configurations.get(CoreStrings.getService(STATS), HighscoreSettings.class);
    }
}
