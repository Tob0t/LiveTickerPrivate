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
    private Constants.ItemType type;
    private HashMap<String, Object> timestampSent;
    private String userId;

    public GameEvent() {}

    public GameEvent(String message, Constants.ItemType type, String userId) {
        this.message = message;
        this.type = type;
        this.info = "";
        HashMap<String, Object> timestampSentObj = new HashMap<String, Object>();
        timestampSentObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampSent = timestampSentObj;
        this.userId = userId;
    }

    public GameEvent(String message, String info, Constants.ItemType type, String userId) {
        this.message = message;
        this.info = info;
        this.type = type;
        HashMap<String, Object> timestampSentObj = new HashMap<String, Object>();
        timestampSentObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampSent = timestampSentObj;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo() {
        return info;
    }

    public String getUserId() {
        return userId;
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
