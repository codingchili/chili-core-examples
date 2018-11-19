package com.codingchili.highscore.controller;

import com.codingchili.highscore.context.HighscoreContext;
import com.codingchili.highscore.model.Constants;
import com.codingchili.highscore.model.HighscoreEntry;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.protocol.*;

import static com.codingchili.core.protocol.RoleMap.PUBLIC;
import static com.codingchili.highscore.model.Constants.ID_LIST;

/**
 * Handler class that is used by the listener to handle incoming messages.
 * The type of the listener can be HTTP/REST, UDP, TCP, WEBSOCKET or CLUSTER.
 *
 * Roles annotation may be placed on the handler or on individual services,
 * the annotation is processed by the protocol. The protocol will then
 * proceed to compare the Role specified to the role returned by the
 * pluggable authorization implementation.
 *
 * The address annotation indicates where the handler is listening. It is
 * picked up by the listener to route requests to the correct handler.
 */
@Roles(PUBLIC)
@Address(Constants.HIGHSCORE_NODE)
public class HighscoreHandler implements CoreHandler {
    private Protocol<Request> protocol = new Protocol<>(this);
    private List<HighscoreEntry> highscore = new ArrayList<>();
    private HighscoreContext context;

    public HighscoreHandler(HighscoreContext context) {
        // call 'annotated' so the protocol processes the annotations for this API.
        // we can still use protocol.use('name', handler); if we want to.
        protocol.annotated(this);
        this.context = context;
    }

    /**
     * This update method is called for requests to /api/update.
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
     * This method to list highscores is called for requests to /api/list
     */
    @Api
    public void list(HighscoreRequest request) {
        request.write(new JsonObject().put(ID_LIST, toJsonArray(highscore)));
    }

    private JsonArray toJsonArray(List<HighscoreEntry> list) {
        JsonArray array = new JsonArray();
        list.forEach(entry -> array.add(Serializer.json(entry)));
        return array;
    }

    /**
     * Required method in all Handlers; may handle authentication etc.
     */
    @Override
    public void handle(Request request) {
        protocol.process(new HighscoreRequest(request));
    }
}