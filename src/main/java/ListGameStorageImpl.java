import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListGameStorageImpl implements GameStorage, Serializable {

    public static List<Game> gamesStorage = new ArrayList<>();

    @Override
    public Game getGame(long id) {
        for (Game game : gamesStorage) {
            if (game.getGameId() == id)
                return game;
        }
        return null;
    }

    @Override
    public void addGame(Game game) {
        gamesStorage.add(game);

        serialization();

    }

    @Override
    public void addRate(Rate rate) {

        for (Game g : gamesStorage) {
            if (g.getGameId() == rate.getGameId()) {
                g.addRate(rate);
            }
        }

        serialization();

    }

    @Override
    public List<Game> getAllGames() {
        List<Game> gameList = new ArrayList<>();

        for (Game game : gamesStorage) {
            gameList.add(game);
        }
        return gameList;
    }

    @Override
    public double getAverageRate(long id) {

        double averageRate = 0;

        for (Game game : gamesStorage) {
            if (game.getGameId() == id) {
                List<Rate> rates = game.getRateList();
                for (Rate o : rates) {
                    averageRate += o.getRate();
                }
                averageRate = averageRate / rates.size();
            }
        }
        return averageRate;
    }

    @Override
    public void deleteGame(long id) {

        boolean flag = false;

        for (Game game : gamesStorage) {

            if (game.getGameId() == id) {
                flag = true;
            }
        }
        if (flag)
            gamesStorage.remove(getGame(id));

        serialization();

    }

    public void serialization() {

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("server.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gamesStorage);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

    }


}
