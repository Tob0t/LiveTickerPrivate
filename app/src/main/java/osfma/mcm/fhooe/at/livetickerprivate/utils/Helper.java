package osfma.mcm.fhooe.at.livetickerprivate.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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

/**
 * Created by Tob0t on 24.02.2016.
 */
public class Helper {
    private static final String LOG_TAG = Helper.class.getSimpleName();

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN);
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm", Locale.GERMAN);
    public static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.GERMAN);


    /**
    * Encode user email to use it as a Firebase key (Firebase does not allow "." in the key name)
    * Encoded email is also used as "userEmail", list and item "owner" value
    */

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    public static String decodeEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    public static boolean checkIfOwner(Game game, String currentUserEmail) {
        return (game.getOwner() != null && game.getOwner().equals(currentUserEmail));
    }

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

    // iterate over all Filter Methods, if they are all true than inflate an icon
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

    /**
     * Show toast msg to users
     */
    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    /*public static HashMap<String, Object> updateGameSet
     (final String listId,
     final String owner, HashMap<String, Object> mapToUpdate,
     String propertyToUpdate, Object valueToUpdate) {

     mapToUpdate.put("/" + "/" + owner + "/"
     + listId + "/" + propertyToUpdate, valueToUpdate);

     return mapToUpdate;
     }*/
}
