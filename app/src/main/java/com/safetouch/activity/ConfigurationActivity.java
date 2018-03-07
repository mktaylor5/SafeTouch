package com.safetouch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.safetouch.R;

/**
 * Created by mktay on 3/5/2018.
 */

public class ConfigurationActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        getSupportActionBar(); // .setDisplayHomeAsUpEnabled(true);
    }
}
