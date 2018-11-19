package com.codingchili.highscore;


import com.codingchili.highscore.context.HighscoreContext;
import com.codingchili.highscore.controller.HighscoreHandler;
import io.vertx.core.Future;

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.context.SystemContext;
import com.codingchili.core.files.Configurations;
import com.codingchili.core.listener.CoreService;
import com.codingchili.core.listener.ListenerSettings;
import com.codingchili.core.listener.transport.RestListener;

/**
 * Service that deploys listeners/handlers as a part of the highscore service.
 */
public class Service implements CoreService {
    private HighscoreContext context;

    public static void main(String[] args) {
        // configure the launcher to warn when it cannot find the conf/service/highscore.json configuration file.
        Configurations.launcher().setWarnOnDefaultsLoaded(true);

        // create a new system context and deploy this class as a service.
        new SystemContext().service(Service::new);
    }

    @Override
    public void init(CoreContext core) {
        // init method that is called with the current context when deployed.
        context = new HighscoreContext(core);
    }

    @Override
    public void start(Future<Void> start) {
        // listener settings for the HTTP REST listener.
        ListenerSettings settings = new ListenerSettings()
                .setPort(8080)
                .setSecure(false);

        // deploy a HTTP REST listener with the configuration.
        // requests are handled by the highscore handler.
        context.listener(() ->
                new RestListener()
                        .settings(() -> settings)
                        .handler(new HighscoreHandler(context)));

        start.complete();
    }
}