package osfma.mcm.fhooe.at.livetickerprivate.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import osfma.mcm.fhooe.at.livetickerprivate.R;
import osfma.mcm.fhooe.at.livetickerprivate.model.Game;
import osfma.mcm.fhooe.at.livetickerprivate.model.User;
import osfma.mcm.fhooe.at.livetickerprivate.ui.BaseActivity;

/**
 * Created by Tob0t on 24.02.2016.
 */
public class Helper {
    private static final String LOG_TAG = Helper.class.getSimpleName();

    // Date and time formatter functions
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN);
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm", Locale.GERMAN);
    public static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.GERMAN);


    /**
    * Encode user email to use it as a Firebase key (Firebase does not allow "." in the key name)
    */
    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    public static String decodeEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    // check if the user is owner of the game
    public static boolean checkIfOwner(Game game, String userId) {
        return (game.getUserId() != null && game.getUserId().equals(userId));
    }

    // transform the gametype to a firebase link
    public static String checkGameType(Constants.GameType _gameType) {
        String gameType = Constants.FIREBASE_URL_PUBLIC_GAMES;
        if(_gameType == Constants.GameType.PRIVATE){
            gameType = Constants.FIREBASE_URL_PRIVATE_GAMES;
        }
        return gameType;
    }
    // Using reflection for the filter parameters
    public static Map<Method,Object> addFilter(Map<Method, Object> hashMap, Class className, String methodName, Object valueParam) {

        Method methodParam = null;
        try {
            methodParam = className.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        hashMap.put(methodParam,valueParam);
        return hashMap;
    }

    // iterate over all Filter Methods, if they are all true than inflate an item
    public static boolean checkAllCondtionsTrue(Object object, Map<Method, Object> mFilter){
        boolean allConditionsTrue = true;

        for(Map.Entry<Method, Object> entry: mFilter.entrySet()) {
            Method methodParam = entry.getKey();
            // handle boolean params
            if(entry.getValue() instanceof Boolean){
                boolean valueParam = (boolean) entry.getValue();
                try {
                    if((boolean) methodParam.invoke(object) != valueParam){
                        allConditionsTrue = false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            // handle String params
            else if(entry.getValue() instanceof String){
                String valueParam = (String) entry.getValue();
                try {
                    if(!(((String) methodParam.invoke(object)).equals(valueParam))){
                        allConditionsTrue = false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return allConditionsTrue;
    }

    // return userId of current user
    public static String getUserId(){
        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        return ref.getAuth().getUid();
    }

    /**
     * Show toast msg to users
     */
    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
}
