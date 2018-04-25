package com.safetouch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    // This method will be executed every time the alarm is fired
    @Override
    public void onReceive(Context context, Intent intent) {
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Toast.makeText(context, "Alarm triggered.", Toast.LENGTH_LONG).show();
    }
}
