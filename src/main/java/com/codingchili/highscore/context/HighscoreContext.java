package com.codingchili.highscore.context;

import io.vertx.core.Vertx;

import com.codingchili.core.Configuration.Strings;
import com.codingchili.core.Context.ServiceContext;
import com.codingchili.core.Files.Configurations;


/**
 * Contexts provides access to the configuration of the running service. It is
 * recommended to leave service() protected and instead provide helper methods to
 * avoid the result of Configurations.get being cached.
 */
public class HighscoreContext extends ServiceContext {
    private static final String STATS = "highscore";

    public HighscoreContext(Vertx vertx) {
        super(vertx);
    }

    @Override
    protected HighscoreSettings service() {
        return Configurations.get(Strings.getService(STATS),
                HighscoreSettings.class);
    }

    public Integer maxCount() {
        return service().maxCount;
    }
}
