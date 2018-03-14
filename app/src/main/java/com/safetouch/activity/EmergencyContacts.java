package com.safetouch.activity;

/**
 * Created by Monica on 3/6/2018.
 */
import android.os.Bundle;
import android.view.MenuItem;

import com.safetouch.R;

public class EmergencyContacts extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts);
        getSupportActionBar();//.setDisplayHomeAsUpEnabled(true);
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
}