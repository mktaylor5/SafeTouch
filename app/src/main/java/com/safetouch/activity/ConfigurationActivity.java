package com.safetouch.activity;

import android.app.Activity;
import android.content.Context;
import android.preference.EditTextPreference;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.safetouch.R;
import com.safetouch.database.AppDatabase;
import com.safetouch.domain.Configuration;

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

    public static class PrefsFragment extends PreferenceFragment{
        private Context mContext;
        private Activity mActivity;
        AppDatabase database;

        public static void setDefaults(String key, String value, Context context) {//set to be able to get in other activities
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
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

                    setDefaults("preset_msg",newMessage,mContext);

                    EditTextPreference editPref = (EditTextPreference)preference;//save preference
                    editPref.setSummary(newMessage);
                    editPref.setText(newMessage);
                    return false;
                }
            });
        }
    }

}