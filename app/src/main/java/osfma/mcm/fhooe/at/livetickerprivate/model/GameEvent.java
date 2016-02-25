package osfma.mcm.fhooe.at.livetickerprivate.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tob0t on 24.02.2016.
 */
public class GameEvent {
    private String currentScore;
    private String info;
    private HashMap<String,Chat> chatMessages;

    public GameEvent() {
        chatMessages = new HashMap<>();
    }

    public GameEvent(String currentScore, String info) {
        this.currentScore = currentScore;
        this.info = info;
    }

    public String getCurrentScore() {
        return currentScore;
    }

    public String getInfo() {
        return info;
    }

    public HashMap<String, Chat> getChatMessages() {
        return chatMessages;
    }
}
