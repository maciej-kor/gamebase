import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response.Status.*;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class GameController {

    private final static String GAME_ID_PARAM = "gameId";

    private GameStorage gameStorage = new ListGameStorageImpl();

    public Response serveGetGameRequest(IHTTPSession session) {

        Map<String, String> requestParameters = session.getParms();
        if (requestParameters.containsKey(GAME_ID_PARAM)) {
            String gameIdParam = requestParameters.get(GAME_ID_PARAM);
            long gameId = 0;

            try {
                gameId = Long.parseLong(gameIdParam);
            } catch (NumberFormatException nfe) {
                System.err.println("Error during convert request parameters: " + nfe);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request parameter 'gameId' have to be a number");
            }

            Game game = gameStorage.getGame(gameId);

            if (game != null) {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = objectMapper.writeValueAsString(game);
                    return newFixedLengthResponse(OK, "application/json", response);
                } catch (JsonProcessingException e) {
                    System.err.println("Error during process request: " + e);
                    return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error can't read game");
                }
            }

            return newFixedLengthResponse(NOT_FOUND, "application/json", "");
        }

        return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Uncorrected request parameters");
    }

    public Response serveAddGameRequest(IHTTPSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        long randomGameId = objectMapper.hashCode();
        Map<String, String> headers = session.getHeaders();

        String lenghtHeader = headers.get("content-length");
        int contentLenght = Integer.valueOf(lenghtHeader);
        byte[] buffer = new byte[contentLenght];

        try {
            session.getInputStream().read(buffer, 0, contentLenght);
            String requestBody = new String(buffer).trim();
            Game requestGame = objectMapper.readValue(requestBody, Game.class);
            requestGame.setGameId(randomGameId);

            gameStorage.addGame(requestGame);

        } catch (Exception e) {
            System.err.println("Error: " + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Game hasn't been added");
        }

        return newFixedLengthResponse(OK, "text/plain", "Game has been added" + randomGameId);

    }

    public Response responseGetAllGamesRequest(IHTTPSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        String response = "";

        try {
            response = objectMapper.writeValueAsString(gameStorage.getAllGames());
        } catch (JsonProcessingException e) {
            System.err.println("Error during process request: " + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error, can't read all games");
        }

        return newFixedLengthResponse(OK, "application/json", response);
    }


    public Response serveAddRateRequest(IHTTPSession session) {

        Map<String, String> headers = session.getHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        String lenghtHeader = headers.get("content-length");
        int contentLenght = Integer.valueOf(lenghtHeader);
        byte[] buffer = new byte[contentLenght];

        try {
            session.getInputStream().read(buffer, 0, contentLenght);
            String requestRate = new String(buffer).trim();
            Rate rate = objectMapper.readValue(requestRate, Rate.class);
            gameStorage.addRate(rate);
        } catch (Exception e) {
            System.err.println("Error: " + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Rate hasn't been added");
        }

        return newFixedLengthResponse(OK, "text/plain", "Rate has been added");
    }

    public Response responseGetAllRates(IHTTPSession session) {

        Map<String, String> requestParameters = session.getParms();
        if (requestParameters.containsKey(GAME_ID_PARAM)) {
            String gameIdParam = requestParameters.get(GAME_ID_PARAM);
            long gameId = 0;

            try {
                gameId = Long.parseLong(gameIdParam);
            } catch (NumberFormatException nfe) {
                System.err.println("Error during convert request parameters: " + nfe);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request parameter 'gameId' have to be a number");
            }

            double averageRate = gameStorage.getAverageRate(gameId);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String result = objectMapper.writeValueAsString(averageRate);
                return newFixedLengthResponse(OK, "application/json", result);
            } catch (Exception e) {

            }
        }
        return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Uncorrected request parameters");
    }

    public Response deleteGameRequest(IHTTPSession session) {

        Map<String, String> requestParam = session.getParms();

        if (requestParam.containsKey("gameId")) {

            String requestParameter = requestParam.get("gameId");
            long gameId = 0;

            try {
                gameId = Long.parseLong(requestParameter);
            } catch (NumberFormatException e) {
                System.err.println("Number format exception: " + '\n' + e);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request parameter 'gameId' have to be a number");
            }

            gameStorage.deleteGame(gameId);
            return newFixedLengthResponse(OK, "text/plain", "Rate has been deleted");

        }
        return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Uncorrected request parameters");
    }

}