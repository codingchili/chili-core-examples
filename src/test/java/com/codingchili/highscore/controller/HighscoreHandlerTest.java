package com.codingchili.highscore.controller;

import com.codingchili.highscore.context.ContextMock;
import com.codingchili.highscore.model.Constants;
import com.codingchili.highscore.model.HighscoreEntry;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import com.codingchili.core.Exception.CoreException;
import com.codingchili.core.Protocol.ResponseStatus;
import com.codingchili.core.Protocol.Serializer;
import com.codingchili.core.Testing.RequestMock;
import com.codingchili.core.Testing.ResponseListener;

import static com.codingchili.highscore.model.Constants.*;

/**
 * @author Robin Duda
 *
 * Sample test for a handler.
 */
@RunWith(VertxUnitRunner.class)
public class HighscoreHandlerTest {
    private static final String PLAYER_NAME = "player";
    private HighscoreHandler handler;
    private ContextMock context;

    @Before
    public void setUp() {
        context = new ContextMock(Vertx.vertx());
        handler = new HighscoreHandler<>(context);
        context.setMaxCount(3);
    }

    @Test
    public void testUpdateList(TestContext test) throws CoreException {
        handle(Constants.ID_UPDATE, (response, status) -> {
            test.assertEquals(ResponseStatus.ACCEPTED, status);
        }, getUpdate(PLAYER_NAME, 50));
    }

    private JsonObject getUpdate(String player, int score) {
        return new JsonObject()
                .put(ID_PLAYER, player)
                .put(ID_SCORE, score);
    }

    @Test
    public void testSortOrder(TestContext test) throws CoreException {
        updateFourTimes();

        handle(Constants.ID_LIST, (response, status) -> {
            List<HighscoreEntry> entries = getHighScoreList(response);

            for (int i = 0; i < 3; i++) {
                HighscoreEntry entry = entries.get(i);
                test.assertEquals(PLAYER_NAME + (3 - i), entry.player);
                test.assertEquals((3 - i) * 10, entry.score);
            }

            test.assertEquals(ResponseStatus.ACCEPTED, status);
        });
    }

    private void updateFourTimes() throws CoreException {
        for (int i = 0; i < 4; i++) {
            handle(Constants.ID_UPDATE, (response, status) -> {
            }, getUpdate(PLAYER_NAME + i, 10 * i));
        }
    }

    private List<HighscoreEntry> getHighScoreList(JsonObject response) {
        return response.getJsonArray(ID_LIST).stream()
                .map(entry -> Serializer.<HighscoreEntry>unpack((JsonObject) entry, HighscoreEntry.class))
                .collect(Collectors.toList());
    }

    @Test
    public void testOldEntriesRemoved(TestContext test) throws CoreException {
        updateFourTimes();

        handle(ID_LIST, (response, status) -> {
            test.assertEquals(3, response.getJsonArray(ID_LIST).size());
        });
    }

    @Test
    public void testChangeLimit(TestContext test) throws CoreException {
        context.setMaxCount(1);
        updateFourTimes();

        handle(ID_LIST, (response, status) -> {
            test.assertEquals(1, response.getJsonArray(ID_LIST).size());
        });
    }

    private void handle(String action, ResponseListener listener) throws CoreException {
        handle(action, listener, new JsonObject());
    }

    /**
     * ResponseListener provides a callback with the response in json format and the status code.
     * RequestMock.get creates a new mock request.
     */
    private void handle(String action, ResponseListener listener, JsonObject payload) throws CoreException {
        handler.handle(RequestMock.get(action, listener, payload));
    }
}