package com.codingchili.highscore.controller;

import com.codingchili.core.listener.Request;
import com.codingchili.core.listener.RequestWrapper;

/**
 * @author Robin Duda
 *
 * A request class extending a ClusterRequest to encapsulate serialization logic.
 */
public class HighscoreRequest implements RequestWrapper {
    private Request request;

    HighscoreRequest(Request request) {
        this.request = request;
    }

    @Override
    public Request request() {
        return request;
    }
}
