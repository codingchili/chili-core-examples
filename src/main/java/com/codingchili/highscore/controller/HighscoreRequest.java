package com.codingchili.highscore.controller;

import com.codingchili.highscore.model.HighscoreEntry;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

import com.codingchili.core.Protocol.*;

import static com.codingchili.highscore.model.Constants.ID_LIST;

/**
 * @author Robin Duda
 *
 * A request class extending a ClusterRequest to encapsulate serialization logic.
 */
class HighscoreRequest extends ClusterRequest {
    HighscoreRequest(Request request) {
        super(request);
    }

    void sendHighscore(List<HighscoreEntry> highscore) {
        write(new JsonObject().put(ID_LIST, toJsonArray(highscore)));
    }

    private JsonArray toJsonArray(List<HighscoreEntry> list) {
        JsonArray array = new JsonArray();
        list.stream().forEach(entry -> array.add(Serializer.json(entry)));
        return array;
    }
}
