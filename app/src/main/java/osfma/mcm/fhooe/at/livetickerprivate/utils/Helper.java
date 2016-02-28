package osfma.mcm.fhooe.at.livetickerprivate.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import osfma.mcm.fhooe.at.livetickerprivate.model.Game;

/**
 * Created by Tob0t on 24.02.2016.
 */
public class Helper {
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

    public static boolean checkIfOwner(Game game, String currentUserEmail) {
        return (game.getOwner() != null && game.getOwner().equals(currentUserEmail));
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
