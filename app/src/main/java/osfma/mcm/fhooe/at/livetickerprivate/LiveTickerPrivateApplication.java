package osfma.mcm.fhooe.at.livetickerprivate;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Tob0t on 23.02.2016.
 */
public class LiveTickerPrivateApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        Firebase.setAndroidContext(this);
        /* Enable disk persistence  */
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
