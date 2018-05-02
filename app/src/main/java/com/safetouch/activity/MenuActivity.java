package com.safetouch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.safetouch.R;

/**
 * Created by mktay on 3/6/2018.
 */

public class MenuActivity extends BluetoothActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.home) {
            String mode = getDefaults("mode_list", getApplicationContext());
            switch(mode.toLowerCase())
            {
                default:
                case "none":
                    startActivity(new Intent(this, HomeActivity.class));
                    break;
                case "parent":
                    startActivity(new Intent(this, ParentActivity.class));
                    break;
                case "medical":
                    startActivity(new Intent(this, MedicalActivity.class));
                    break;
                case "personal":
                    startActivity(new Intent(this, MainActivity.class));
                    break;
            }
            //intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
        }
        else if (id == R.id.emergency_contacts) {
            Intent intent = new Intent(this, EmergencyContacts.class);
            startActivity(intent);
        }
        else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ConfigurationActivity.class);
            startActivity(intent);
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    public static String getDefaults(String key, Context context) {//to get string from settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
