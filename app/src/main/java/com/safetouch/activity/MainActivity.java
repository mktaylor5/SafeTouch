package com.safetouch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.safetouch.R;
import com.safetouch.domain.Configuration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.emergency_contacts) {
            Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT).show();
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
