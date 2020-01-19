
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

import java.io.Serializable;

import static fi.iki.elonen.NanoHTTPD.Method.GET;
import static fi.iki.elonen.NanoHTTPD.Method.POST;
import static fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND;
import static fi.iki.elonen.NanoHTTPD.Method.DELETE;

public class RequestUrlMapper {

    private final static String GET_GAME_URL = "/game/get";
    private final static String ADD_GAME_URL = "/game/add";
    private final static String RATE_GAME_URL = "/game/rate";
    private final static String GET_ALL_GAMES_URL = "/game/getAll";
    private final static String GET_AVERAGE_RATE_URL = "/game/getAllRates";
    private final static String DELETE_GAME_URL = "/game/delete";


    private GameController gameController = new GameController();

    public Response delegateRequest(IHTTPSession session) {

        if (session.getMethod().equals(GET) && session.getUri().equals(GET_GAME_URL)) {
            return gameController.serveGetGameRequest(session);
        } else if (session.getMethod().equals(POST) && session.getUri().equals(ADD_GAME_URL)){
            return gameController.serveAddGameRequest(session);
        } else if (session.getMethod().equals(POST) && session.getUri().equals(RATE_GAME_URL)){
            return gameController.serveAddRateRequest(session);
        }  else if(session.getMethod().equals(GET) && session.getUri().equals(GET_ALL_GAMES_URL)){
            return gameController.responseGetAllGamesRequest(session);
        } else if (session.getMethod().equals(GET) && session.getUri().equals(GET_AVERAGE_RATE_URL)){
            return gameController.responseGetAllRates(session);
        } else if (session.getMethod().equals(DELETE) && session.getUri().equals(DELETE_GAME_URL)){
            return gameController.deleteGameRequest(session);
        }

        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, "text/plain", "Not found");

    }

}
