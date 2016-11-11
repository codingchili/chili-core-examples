package com.codingchili.highscore;


/**
 * Example using this microservice with REST
 *
 * >>   examplerouter.com/?target=statistics.node&action=update&player=username&score=5000
 * <<   {"status":"ACCEPTED"}
 *
 * >>   examplerouter.com/?target=statistics.node&action=list
 * <<   {"list":[{"player":"player100","score":1006005}, ... ],"status":"ACCEPTED"}
 */
import com.codingchili.highscore.context.HighscoreContext;
import com.codingchili.highscore.controller.HighscoreHandler;
import io.vertx.core.Future;

import com.codingchili.core.Launcher;
import com.codingchili.core.Protocol.ClusterNode;


public class Service extends ClusterNode {
    /**
     * Call the launcher from core, which loads the launcher.json and deploys
     * all configured services. Any ClusterVerticle on the classpath may be added
     * to the configuration file.
     */
    public static void main(String[] args) {
        Launcher.main(args);
    }

    /**
     * Creates the context for this microservice and deploys a handler
     * within a ClusterListener.
     */
    @Override
    public void start(Future<Void> future) {
        HighscoreContext context = new HighscoreContext(vertx);
        context.deploy(new HighscoreHandler<>(context));
    }
}