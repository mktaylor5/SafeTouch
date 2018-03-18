package com.safetouch.activity;

/**
 * Created by Monica on 3/6/2018.
 */
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.view.MenuItem;

import com.safetouch.R;
import com.safetouch.database.AppDatabase;
import com.safetouch.domain.Contact;

import static com.safetouch.database.AppDatabase.DB_NAME;

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

    public void createContact(Contact contact) {
        // instance of database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DB_NAME).build();

        db.getContactDao().insert(contact);
    }
}