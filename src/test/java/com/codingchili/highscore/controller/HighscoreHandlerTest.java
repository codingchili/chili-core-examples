package com.codingchili.highscore.controller;

import com.codingchili.highscore.context.HighscoreContextMock;
import com.codingchili.highscore.model.Constants;
import com.codingchili.highscore.model.HighscoreEntry;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import com.codingchili.core.protocol.ResponseStatus;
import com.codingchili.core.protocol.Serializer;
import com.codingchili.core.testing.*;

import static com.codingchili.highscore.model.Constants.*;

/**
 * @author Robin Duda
 *
 * Sample test for a handler, as handlers are decoupled from the underlying
 * transports its very easy to write unit tests for them.
 */
@RunWith(VertxUnitRunner.class)
public class HighscoreHandlerTest {
    private static final String PLAYER_NAME = "player";
    private HighscoreHandler handler;
    private HighscoreContextMock context;

    @Before
    public void setUp() {
        context = new HighscoreContextMock(new ContextMock());
        handler = new HighscoreHandler(context);
        context.settings().setMaxCount(3);
    }

    @After
    public void tearDown(TestContext test) {
        context.close(test.asyncAssertSuccess());
    }

    @Test
    public void testUpdateList(TestContext test) {
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
    public void testSortOrder(TestContext test) {
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

    private void updateFourTimes() {
        for (int i = 0; i < 4; i++) {
            handle(Constants.ID_UPDATE, (response, status) -> {
            }, getUpdate(PLAYER_NAME + i, 10 * i));
        }
    }

    private List<HighscoreEntry> getHighScoreList(JsonObject response) {
        return response.getJsonArray(ID_LIST).stream()
                .map(entry -> Serializer.unpack((JsonObject) entry, HighscoreEntry.class))
                .collect(Collectors.toList());
    }

    @Test
    public void testOldEntriesRemoved(TestContext test) {
        updateFourTimes();

        handle(ID_LIST, (response, status) -> {
            test.assertEquals(3, response.getJsonArray(ID_LIST).size());
        });
    }

    @Test
    public void testChangeLimit(TestContext test) {
        context.settings().setMaxCount(1);
        updateFourTimes();

        handle(ID_LIST, (response, status) -> {
            test.assertEquals(1, response.getJsonArray(ID_LIST).size());
        });
    }

    private void handle(String action, ResponseListener listener) {
        handle(action, listener, new JsonObject());
    }

    /**
     * ResponseListener provides a callback with the response in json format and the status code.
     * RequestMock.get creates a new mock request.
     */
    private void handle(String action, ResponseListener listener, JsonObject payload) {
        handler.handle(RequestMock.get(action, listener, payload));
    }
}