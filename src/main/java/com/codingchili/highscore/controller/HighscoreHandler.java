package com.codingchili.highscore.controller;

import com.codingchili.highscore.context.HighscoreContext;
import com.codingchili.highscore.model.HighscoreEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.codingchili.core.Exception.CoreException;
import com.codingchili.core.Protocol.*;

import static com.codingchili.highscore.model.Constants.*;

/**
 * Handler class that is used by the ClusterListener to handle incoming messages.
 * Handlers are registered to cluster-wide addresses.
 */
public class HighscoreHandler<T extends HighscoreContext> extends AbstractHandler<T> {
    private Protocol<RequestHandler<HighscoreRequest>> protocol = new Protocol<>();
    private List<HighscoreEntry> highscore = new ArrayList<>();

    public HighscoreHandler(T context) {
        super(context, HIGHSCORE_NODE);

        /** set up routes for this handler */
        protocol.use(ID_LIST, this::list)
                .use(ID_UPDATE, this::update);
    }

    /**
     * Handler method for the update action, called with a raw request object.
     * When using raw requests manual serialization is needed, it is recommended
     * to extend the request class and provide helper methods for serialization.
     */
    private void update(Request request) {
        highscore.add(Serializer.unpack(request.data(), HighscoreEntry.class));

        highscore = highscore.stream()
                .sorted(((o1, o2) -> o2.score.compareTo(o1.score)))
                .distinct()
                .limit(context.maxCount())
                .collect(Collectors.toList());

        request.accept();
    }

    /**
     * Handler method to list all the current highscore entries on the server.
     */
    private void list(HighscoreRequest request) {
        request.sendHighscore(highscore);
    }

    /**
     * Required method in all Handlers; may handle authentication etc.
     */
    @Override
    public void handle(Request request) throws CoreException {
        protocol.get(request.action()).handle(new HighscoreRequest(request));
    }
}