package com.safetouch.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.safetouch.R;
import com.safetouch.domain.Configuration;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    int MID = 0;
    // This method will be executed every time the alarm is fired
    @Override
    public void onReceive(Context context, Intent intent) {
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Toast.makeText(context, "Alarm triggered.", Toast.LENGTH_LONG).show();

//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent notificationIntent = new Intent(context, Configuration.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.person2)
//                .setContentTitle("Alarm Fired")
//                .setContentText("Events to be Performed")
//                .setSound(alarmSound)
//                .setAutoCancel(true).setWhen(when)
//                .setContentIntent(pendingIntent)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
//
//        notificationManager.notify(MID, mNotifyBuilder.build());
//        MID++;
    }
}
