package com.safetouch.activity;

import android.content.Intent;
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
            //Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.emergency_contacts) {
            Intent intent = new Intent(this, EmergencyContacts.class);
            startActivity(intent);
            //Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT).show);
        }
        else if (id == R.id.action_settings) {
            //Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ConfigurationActivity.class);
            startActivity(intent);
        }

        return true;
        //return super.onOptionsItemSelected(item);
    }
}
