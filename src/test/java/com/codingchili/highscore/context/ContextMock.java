package com.codingchili.highscore.context;

import io.vertx.core.Vertx;

/**
 * @author Robin Duda
 *
 * Example of mocking a context, normally methods that return a storage
 * engine that requires a server to be up and running be mocked.
 *
 * Less is more when mocking.
 */
public class ContextMock extends HighscoreContext {
    private HighscoreSettings settings;

    public ContextMock(Vertx vertx) {
        super(vertx);

        settings = new HighscoreSettings();
        settings.maxCount = 3;
    }

    @Override
    protected HighscoreSettings service() {
        return settings;
    }

    public void setMaxCount(int count) {
        settings.maxCount = count;
    }
}
