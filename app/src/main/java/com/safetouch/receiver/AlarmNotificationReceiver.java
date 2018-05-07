package com.safetouch.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.safetouch.R;
import com.safetouch.activity.ConfigurationActivity;
import com.safetouch.activity.MedicalActivity;
import com.safetouch.domain.Configuration;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    int MID = 0;

    // This method will be executed every time the alarm is fired
    @Override
    public void onReceive(Context context, Intent intent) {
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Toast.makeText(context, "Alarm triggered.", Toast.LENGTH_LONG).show();
        Log.i("alarms:", "RECEIVED");

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, ConfigurationActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.person2)
                .setContentTitle("Check-in Notice")
                .setContentText("It's time for a check-in!")
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        assert notificationManager != null;
        notificationManager.notify(MID, mNotifyBuilder.build());
        MID++;

        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = 1; //Integer.parseInt(getDefaults("checkin_interval", context));
        //int interval = 1;
        assert alarms != null;
        alarms.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + (interval * 60000),
                pendingIntent);

        MedicalActivity medical = new MedicalActivity();
        medical.checkInDone(context);

    }

    public static String getDefaults(String key, Context context) {//to get string from settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
