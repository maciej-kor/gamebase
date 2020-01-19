import java.util.List;

public interface GameStorage {

    Game getGame(long id);

    List<Game> getAllGames();

    void addGame(Game game);

    void addRate(Rate rate);

    double getAverageRate(long id);

    void deleteGame(long id);



}
