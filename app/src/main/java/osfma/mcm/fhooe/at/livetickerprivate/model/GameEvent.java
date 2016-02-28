package osfma.mcm.fhooe.at.livetickerprivate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.Date;
import java.util.HashMap;

import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;


/**
 * Created by Tob0t on 24.02.2016.
 */
public class GameEvent {
    private String message;
    private String info;
    private String author;
    private Constants.ItemType type;
    private HashMap<String, Object> timestampSent;

    public GameEvent() {}

    public GameEvent(String message, String author, Constants.ItemType type) {
        this.message = message;
        this.author = author;
        this.type = type;
        HashMap<String, Object> timestampSentObj = new HashMap<String, Object>();
        timestampSentObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampSent = timestampSentObj;
    }

    public GameEvent(String message, String info, String author, Constants.ItemType type) {
        this.message = message;
        this.info = info;
        this.author = author;
        this.type = type;
        HashMap<String, Object> timestampSentObj = new HashMap<String, Object>();
        timestampSentObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampSent = timestampSentObj;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo() {
        return info;
    }

    public String getAuthor() {
        return author;
    }

    public Constants.ItemType getType() {
        return type;
    }

    public HashMap<String, Object> getTimestampSent() {
        return timestampSent;
    }

    @JsonIgnore
    public long getTimestampSentLong() {
        return (long) timestampSent.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }
}
