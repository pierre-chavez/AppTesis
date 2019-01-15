package com.example.uees2.myapplication;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationPayload;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;

public class NotificationExtenderBareBones extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        OverrideSettings overrideSettings = new OverrideSettings();
        //Establecer color a la notificacion
        /*overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                return builder.setColor(new BigInteger("FF9900FF", 16).intValue());
            }
        };*/

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}
