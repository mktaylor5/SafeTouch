package com.safetouch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.safetouch.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        String mode = getDefaults("mode_list", getApplicationContext());
                        switch(mode.toLowerCase())
                        {
                            default:
                            case "none":
                                //preferences.
                                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                                break;
                            case "parent":
                                startActivity(new Intent(WelcomeActivity.this, ParentActivity.class));
                                break;
                            case "medical":
                                startActivity(new Intent(WelcomeActivity.this, MedicalActivity.class));
                                break;
                            case "personal":
                                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                break;
                        }
                        //startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                    }
                },
                3000
        );
    }

    public static String getDefaults(String key, Context context) {//to get string from settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}