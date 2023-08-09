package aarav.lju.app;

import android.app.Application;

import com.onesignal.OneSignal;

public class ApplicationClass extends Application {
    private static final String ONESIGNAL_APP_ID = "51c62651-038d-4c5f-b443-925f81b5d6c0";

    @Override
    public void onCreate() {
        super.onCreate();


        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)

}

    }
