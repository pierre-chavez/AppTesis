package com.example.uees2.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String numeroContacto = "";

        if (data != null) {
            Log.i("OneSignal", "numeroContacto");
            numeroContacto = data.optString("numeroContacto", null);
            if (numeroContacto != null) {
                Log.i("OneSignal", "numeroContacto: " + numeroContacto);
            }
        }
        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignal", "Button id: " + result.action.actionID);
            if (result.action.actionID.equals("1") && !numeroContacto.isEmpty() && !numeroContacto.equals("")) {
                Log.i("OneSignal", "Llamando... " + numeroContacto);
                Context c = Dashboard.getContext();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + numeroContacto));
                Log.e("intent", "" + intent.toString());
                Log.e("context", "" + c.toString());

            } else if (result.action.actionID.equals("2")) {
                Context c = Dashboard.getContext();
                Intent intent = new Intent(c, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            }
        }

    }

}
