import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    private long gameId;
    private String title;
    private String producer;
    private List<Rate> rateList = new ArrayList<>();
    private Rate rate;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setRateList(List<Rate> rateList) {
        this.rateList = rateList;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public void addRate(Rate rate) {
        rateList.add(rate);
    }

    public List<Rate> getRateList(){
        return rateList;
    }

}
