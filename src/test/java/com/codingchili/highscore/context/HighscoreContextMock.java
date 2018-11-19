package com.codingchili.highscore.context;

import com.codingchili.core.context.CoreContext;

/**
 * @author Robin Duda
 *
 * Example of mocking a context, normally methods that return a storage
 * engine that requires a server to be up and running be mocked.
 *
 * Less is more when mocking.
 */
public class HighscoreContextMock extends HighscoreContext {
    private HighscoreSettings settings;

    public HighscoreContextMock(CoreContext context) {
        super(context);

        settings = new HighscoreSettings();
        settings.setMaxCount(3);
    }

    @Override
    public HighscoreSettings settings() {
        return settings;
    }
}
