package osfma.mcm.fhooe.at.livetickerprivate.model;

import java.util.HashMap;

/**
 * Created by Tob0t on 28.02.2016.
 */
public class User {
    private String name;
    private String email;
    private HashMap<String, Object> timestampJoined;

    public User() {
    }

    public User(String name, String email, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.email = email;
        this.timestampJoined = timestampJoined;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }
}
