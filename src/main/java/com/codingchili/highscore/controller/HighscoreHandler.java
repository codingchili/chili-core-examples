package com.codingchili.highscore.controller;

import com.codingchili.highscore.context.HighscoreContext;
import com.codingchili.highscore.model.Constants;
import com.codingchili.highscore.model.HighscoreEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;

import static com.codingchili.core.protocol.RoleMap.PUBLIC;

/**
 * Handler class that is used by the ClusterListener to handle incoming messages.
 * Handlers are registered to cluster-wide addresses.
 */
@Roles(PUBLIC)
@Address(Constants.HIGHSCORE_NODE)
public class HighscoreHandler implements CoreHandler {
    private Protocol<Request> protocol = new Protocol<>(this);
    private List<HighscoreEntry> highscore = new ArrayList<>();
    private HighscoreContext context;

    public HighscoreHandler(HighscoreContext context) {
        protocol.annotated(this);
        this.context = context;
    }

    /**
     * Handler method for the update action, called with a raw request object.
     * When using raw requests manual serialization is needed, it is recommended
     * to extend the request class and provide helper methods for serialization.
     */
    @Api
    public void update(HighscoreRequest request) {
        highscore.add(Serializer.unpack(request.data(), HighscoreEntry.class));

        highscore = highscore.stream()
                .sorted(((o1, o2) -> o2.score.compareTo(o1.score)))
                .distinct()
                .limit(context.settings().getMaxCount())
                .collect(Collectors.toList());

        request.accept();
    }

    /**
     * Handler method to list all the current highscore entries on the server.
     */
    @Api
    public void list(HighscoreRequest request) {
        request.sendHighscore(highscore);
    }

    /**
     * Required method in all Handlers; may handle authentication etc.
     */
    @Override
    public void handle(Request request) {
        protocol.process(new HighscoreRequest(request));
    }
}