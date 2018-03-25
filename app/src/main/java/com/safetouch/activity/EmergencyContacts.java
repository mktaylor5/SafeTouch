package com.safetouch.activity;

/**
 * Created by Monica on 3/6/2018.
 */
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.safetouch.R;
import com.safetouch.database.AppDatabase;
import com.safetouch.domain.Contact;

import static com.safetouch.database.AppDatabase.DB_NAME;


public class EmergencyContacts extends MenuActivity {

String[] dataArray = {"Sample" , "John Doe", "Jane Doe"};//Sample inputs for now. Will need to change later

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.emergency_contacts);
    getSupportActionBar();//.setDisplayHomeAsUpEnabled(true);

    FloatingActionButton fab = findViewById(R.id.contacts_button);
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("Awesome","button!");
            //add button function later
        }
    });

    ListView listView = (ListView)findViewById(R.id.contacts_listview);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataArray);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            //put action here! Will change later
            Log.i("Awesome",dataArray[position]);
        }
    });

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