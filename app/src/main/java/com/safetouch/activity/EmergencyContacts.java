package com.safetouch.activity;

/**
 * Created by Monica on 3/6/2018.
 */
import android.os.Bundle;

import com.safetouch.R;

public class EmergencyContacts extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts);
        getSupportActionBar(); // .setDisplayHomeAsUpEnabled(true);
    }
}