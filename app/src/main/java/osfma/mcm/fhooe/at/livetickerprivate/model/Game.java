package osfma.mcm.fhooe.at.livetickerprivate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.HashMap;

import osfma.mcm.fhooe.at.livetickerprivate.utils.Constants;

/**
 * Created by Tob0t on 23.02.2016.
 */
public class Game {
    private String sportType;
    private String team1;
    private String team2;
    private long dateAndTime;
    private boolean started;
    private boolean finished;
    private HashMap<String, GameSet> gameSets;
    private HashMap<String, Object> timestampLastChanged;
    private String userId;

    public Game() {
        gameSets = new HashMap<String, GameSet>();
    }

    public Game(String sportType, String team1, String team2, long dateAndTime, int numberOfGameSets, String userId) {
        this.sportType = sportType;
        this.team1 = team1;
        this.team2 = team2;
        this.dateAndTime = dateAndTime;
        started = false;
        finished = false;
        gameSets = new HashMap<String, GameSet>();
        initializeGameSets(numberOfGameSets);
        HashMap<String, Object> timestampLastChangedObj = new HashMap<String, Object>();
        timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampLastChangedObj;
        this.userId = userId;
    }


    public String getSportType() {
        return sportType;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public long getDateAndTime() {
        return dateAndTime;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }

    public HashMap<String, GameSet> getGameSets() {
        return gameSets;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    public String getUserId() {
        return userId;
    }

    @JsonIgnore
    public long getTimestampLastChangedLong() {
        return (long) timestampLastChanged.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }

    @JsonIgnore
    private void initializeGameSets(int numberOfGameSets) {
        for(int i=0; i<numberOfGameSets;i++) {
            gameSets.put(Constants.GAMESETS_LIST.get(i), new GameSet(0,0));
        }
    }
}
