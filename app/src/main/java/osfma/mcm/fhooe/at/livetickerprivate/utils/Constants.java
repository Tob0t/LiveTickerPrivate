package osfma.mcm.fhooe.at.livetickerprivate.utils;

import java.util.Arrays;
import java.util.List;

import osfma.mcm.fhooe.at.livetickerprivate.BuildConfig;

/**
 * Created by Tob0t on 23.02.2016.
 * Constants class store most important strings and paths of the app
 */
public final class Constants {
    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;

    /**
     * Constants for Firebase object properties
     */
    public static final String FIREBASE_LOCATION_PUBLIC_GAMES = "public_games";
    public static final String FIREBASE_LOCATION_PRIVATE_GAMES = "private_games";
    public static final String FIREBASE_LOCATION_GAMES_EVENTS = "games_events";
    public static final String FIREBASE_LOCATION_GAMES_GAMESETS = "gameSets";
    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_LOCATIN_USER_TYPING_INDICATOR = "typingIndicator";

    public static final String FIREBASE_PROPERTY_GAMES_SPORTTYPE = "sportType";
    public static final String FIREBASE_PROPERTY_GAMES_TEAM1 = "team1";
    public static final String FIREBASE_PROPERTY_GAMES_TEAM2 = "team2";
    public static final String FIREBASE_PROPERTY_GAMES_DATEANDTIME = "dateAndTime";

    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    public static final String FIREBASE_PROPERTY_GAMES_STARTED = "started";
    public static final String FIREBASE_PROPERTY_GAMES_FINISHED = "finished";
    public static final String FIREBASE_PROPERTY_GAMES_GAMESETS_SCORETEAM1 = "scoreTeam1";
    public static final String FIREBASE_PROPERTY_GAMES_GAMESETS_SCORETEAM2 = "scoreTeam2";
    public static final String FIREBASE_PROPERTY_GAMES_GAMESETS_ACTIVE = "active";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";

    public static final String FIREBASE_PROPERTY_GAMES_EVENTS_INFO = "info";

    public static final String FIREBASE_PROPERTY_USERS_NAME = "name";


    public static final String FIREBASE_URL_PUBLIC_GAMES = FIREBASE_URL + "/" + FIREBASE_LOCATION_PUBLIC_GAMES;
    public static final String FIREBASE_URL_PRIVATE_GAMES = FIREBASE_URL + "/" + FIREBASE_LOCATION_PRIVATE_GAMES;
    public static final String FIREBASE_URL_GAMES_EVENTS = FIREBASE_URL + "/" + FIREBASE_LOCATION_GAMES_EVENTS;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_URL_USER_TYPING_INDICATOR = FIREBASE_URL + "/" + FIREBASE_LOCATIN_USER_TYPING_INDICATOR;


    /**
     * Constants for bundles, extras and shared preferences keys
     */
    public static final String KEY_LIST_ID = "GAME_ID";
    public static final String KEY_GOOGLE_EMAIL = "GOOGLE_EMAIL";
    public static final String KEY_PROVIDER = "PROVIDER";
    public static final String KEY_SIGNUP_EMAIL = "SIGNUP_EMAIL";
    public static final String KEY_GAME_TYPE = "GAME_TYPE";
    public static final String KEY_USER_ID = "USER_ID";

    /**
     * Constants for Firebase login
     */
    public static final String PASSWORD_PROVIDER = "password";
    public static final String GOOGLE_PROVIDER = "google";
    public static final String PROVIDER_DATA_DISPLAY_NAME = "displayName";
    public static final String PROVIDER_DATA_PROFILE_IMAGE_URL = "profileImageURL";

    /**
     * Constants for GameStates
     */
    public static final String GAME_STATE = "GameState";
    public static final String GAMES_RUNNING = "Running";
    public static final String GAMES_FUTURE = "Future";
    public static final String GAMES_FINISHED = "Finished";

    /**
     * Methods of Game for Reflection
     */
    public static final String METHOD_GAME_STARTED = "isStarted";
    public static final String METHOD_GAME_FINISHED = "isFinished";
    public static final String METHOD_GAME_OWNER = "getUserId";

    /**
     * Constants for GamyType
     */
    public static final String GAME_TYPE = "GameType";
    public static final String GAME_ID = "GameId";
    public static final int SET_COUNT_DEFAULT = 3;

    /**
     * Constants for Settings
     */
    public static final String USERNAME = "username";

    /**
     * Enums
     */
    public enum Navigate{
        NEXT, PREVIOUS
    }

    public enum Team{
        TEAM1, TEAM2
    }

    public enum ItemType{
        SCORE, CHAT, INFO
    }

    public enum GameType {
        PUBLIC, PRIVATE
    }

    public static final String GAMESET_ONE = "SET_01";
    public static final String GAMESET_TWO = "SET_02";
    public static final String GAMESET_THREE = "SET_03";
    public static final String GAMESET_FOUR = "SET_04";
    public static final String GAMESET_FIVE = "SET_05";
    public static final String GAMESET_SIX = "SET_06";


    // Order of the GameSets
    public static List<String> GAMESETS_LIST = Arrays.asList(
            GAMESET_ONE,
            GAMESET_TWO,
            GAMESET_THREE,
            GAMESET_FOUR,
            GAMESET_FIVE,
            GAMESET_SIX
    );

}
