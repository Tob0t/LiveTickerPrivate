package osfma.mcm.fhooe.at.livetickerprivate.model;

import java.util.ArrayList;

/**
 * Created by Tob0t on 24.02.2016.
 */
public class GameEvent {
    private String currentScore;
    private String info;
    private ArrayList<String,String> messages;
    //private String message;
    //private String owner;

    public GameEvent() {
    }

    public GameEvent(String currentScore, String info, String message) {
        this.currentScore = currentScore;
        this.info = info;
        ArrayList<String,String> = new ArrayList<String,String>();
    }

    public String getCurrentScore() {
        return currentScore;
    }

    public String getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    public String getOwner() {
        return owner;
    }
}
