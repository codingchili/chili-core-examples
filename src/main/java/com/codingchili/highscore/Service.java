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
 * Example using this microservice with REST
 * <p>
 * >>   examplerouter.com/?target=statistics.node&action=update&player=username&score=5000
 * <<   {"status":"ACCEPTED"}
 * <p>
 * >>   examplerouter.com/?target=statistics.node&action=list
 * <<   {"list":[{"player":"player100","score":1006005}, ... ],"status":"ACCEPTED"}
 */
public class Service implements CoreService {
    private HighscoreContext context;

    /**
     * Call the launcher from core, which loads the launcher.json and deploys
     * all configured services. Any ClusterVerticle on the classpath may be added
     * to the configuration file.
     */
    public static void main(String[] args) {
        Configurations.launcher().setWarnOnDefaultsLoaded(true);
        new SystemContext().service(Service::new);
    }

    @Override
    public void init(CoreContext core) {
        context = new HighscoreContext(core);
    }

    @Override
    public void start(Future<Void> start) {
        context.listener(() ->
                new RestListener()
                        .settings(() ->
                                new ListenerSettings()
                                        .setPort(8080)
                                        .setSecure(false))
                        .handler(new HighscoreHandler(context)));

        start.complete();
    }
}