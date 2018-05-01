package com.safetouch.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.EditTextPreference;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
//import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.safetouch.R;
import com.safetouch.database.AppDatabase;
import com.safetouch.receiver.AlarmNotificationReceiver;

import java.util.Date;
//import java.util.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
//import com.safetouch.domain.Configuration;
//import com.safetouch.preference.TimePreference;
//import com.safetouch.preference.TimePreferenceDialogFragmentCompat;

/**
 * Created by mktay on 3/5/2018.
 */

public class ConfigurationActivity extends MenuActivity {
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment()).commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//for back on actionbar
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();// go to parent activity.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        private Context mContext;
        private Activity mActivity;
        AppDatabase database;

        //set to be able to get EditText and List Preferences in other activities
        public static void setDefaults(String key, String value, Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
        {
            Toast.makeText(getActivity(), "Change saved.", Toast.LENGTH_LONG).show();
            setRecurringAlarms(mContext);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            mContext = this.getActivity();
            mActivity = this.getActivity();

            final EditTextPreference msg = (EditTextPreference) getPreferenceScreen().findPreference("preset_msg");
            msg.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {//when new preset msg entered
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    String newMessage = o.toString();
                    //Configuration con = new Configuration();
                    //con.setEmergencyText(newMessage);
                    //database.getConfigurationDao().insert(con);

                    setDefaults("preset_msg", newMessage, mContext);

                    EditTextPreference editPref = (EditTextPreference) preference;//save preference
                    editPref.setSummary(newMessage);
                    editPref.setText(newMessage);
                    return true;
                }
            });

            final ListPreference mode = (ListPreference) getPreferenceScreen().findPreference("mode_list");
            mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {//when new mode entered
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    String newMessage = o.toString();
                    setDefaults("mode_list", newMessage, mContext);

                    ListPreference editPref = (ListPreference) preference;//saves new mode
                    editPref.setSummary(newMessage);
                    editPref.setValue(newMessage);
                    return true;
                }
            });

            final EditTextPreference startTime = (EditTextPreference) getPreferenceScreen().findPreference("checkin_start");
            startTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {//when new preset msg entered
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    String newMessage = o.toString();
                    //Configuration con = new Configuration();
                    //con.setCheckInStart(newMessage);
                    //database.getConfigurationDao().insert(con);

                    setDefaults("checkin_start", newMessage, mContext);

                    EditTextPreference editPref = (EditTextPreference) preference;//save preference
                    editPref.setSummary(newMessage);
                    editPref.setText(newMessage);

                    //setRecurringAlarms(mContext);

                    return true;
                }
            });

            final EditTextPreference endTime = (EditTextPreference) getPreferenceScreen().findPreference("checkin_end");
            endTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {//when new preset msg entered
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    String newMessage = o.toString();
                    //Configuration con = new Configuration();
                    //con.setCheckInStart(newMessage);
                    //database.getConfigurationDao().insert(con);

                    setDefaults("checkin_end", newMessage, mContext);

                    EditTextPreference editPref = (EditTextPreference) preference;//save preference
                    editPref.setSummary(newMessage);
                    editPref.setText(newMessage);

                    //setRecurringAlarms(mContext);

                    return true;
                }
            });

            final EditTextPreference interval = (EditTextPreference) getPreferenceScreen().findPreference("checkin_interval");
            interval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {//when new preset msg entered
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    String newMessage = o.toString();
                    //Configuration con = new Configuration();
                    //con.setCheckInStart(newMessage);
                    //database.getConfigurationDao().insert(con);

                    setDefaults("checkin_interval", newMessage, mContext);

                    EditTextPreference editPref = (EditTextPreference) preference;//save preference
                    editPref.setSummary(newMessage);
                    editPref.setText(newMessage);

                    //setRecurringAlarms(mContext);

                    return true;
                }
            });

            //to get SwitchPreference in other activities:
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getsApplicationContext());
            //boolean test = sharedPreferences.getBoolean("key", false);

            final SwitchPreference buttonOnOff = (SwitchPreference) getPreferenceScreen().findPreference("button");
            buttonOnOff.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {//turn on off button
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SwitchPreference editPref = (SwitchPreference) preference;//for saving
                    if (buttonOnOff.isChecked()) {
                        editPref.setSummary("OFF");
                        editPref.setChecked(false);
                    } else {
                        editPref.setSummary("ON");
                        editPref.setChecked(true);
                    }
                    return true;
                }
            });

            final SwitchPreference alarmOnOff = (SwitchPreference) getPreferenceScreen().findPreference("alarm");
            alarmOnOff.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {//turn on off alarm
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SwitchPreference editPref = (SwitchPreference) preference;//for saving
                    if (alarmOnOff.isChecked()) {
                        editPref.setSummary("OFF");
                        editPref.setChecked(false);
                    } else {
                        editPref.setSummary("ON");
                        editPref.setChecked(true);
                    }
                    return true;
                }
            });

            final SwitchPreference escortOnOFF = (SwitchPreference) getPreferenceScreen().findPreference("escort");
            escortOnOFF.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {//turn on off button
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    SwitchPreference editPref = (SwitchPreference) preference;//for saving
                    if(escortOnOFF.isChecked()){
                        editPref.setSummary("OFF");
                        editPref.setChecked(false);
                    }else{//show dialog that escort mode is on
                        editPref.setSummary("ON");
                        editPref.setChecked(true);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                        builder1.setMessage("Hold down button to start. Once the button pressed, it must be held down until correct pin entered or " +
                                "else contacts will be notified in 30 seconds. ")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mActivity.finish();
                                        startActivity(mActivity.getParentActivityIntent());//sends to home page
                                    }
                                });
                        AlertDialog alert = builder1.create();
                        alert.setTitle("Escort Mode Turned ON.");
                        alert.show();

                    }
                    return true;
                }
            });
        }

        private void setRecurringAlarms(Context context) {
            Log.i("alarms:", "setting alarms");
            int startHour = 4;
            int startMinute = 15;
            String startPeriod = "AM";
            if (startPeriod.toLowerCase().equals("pm")) {
                startHour += 12;
            }
            int interval = 1;
            int endHour = 4;
            int endMinute = 40;
            String endPeriod = "AM";
            if (endPeriod.toLowerCase().equals("pm")) {
                endHour += 12;
            }
            Calendar updateTime = Calendar.getInstance();
            updateTime.set(Calendar.HOUR_OF_DAY, startHour);
            updateTime.set(Calendar.MINUTE, startMinute);

            Intent downloader = new Intent(context, AlarmNotificationReceiver.class);
            PendingIntent recurringNotification = PendingIntent.getBroadcast(context, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarms = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat f = new SimpleDateFormat("HH:mm");
            long min = 0;
            long difference;
            try {
                Date test1 = (Date) f.parse(startHour+":"+startMinute);
                Date test2 = (Date) f.parse(endHour+":"+endMinute);
                difference = (test2.getTime() - test1.getTime()) / 1000;
                long hours = difference % (24 * 3600) / 3600; // Calculating Hours
                long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
                min = minute + (hours * 60);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long duration = min;
            int iterations = (int)duration/interval;
            for (int i = 0; i < iterations; i++)
            {
                updateTime.add(Calendar.MINUTE, interval);
                assert alarms != null;
                alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        updateTime.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,
                        recurringNotification);
                Log.i("alarms: ", String.valueOf(alarms!=null));
            }
        }
    }

}