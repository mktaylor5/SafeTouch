package com.safetouch.activity;

/**
 * Created by Monica on 3/6/2018.
 */

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

import java.util.List;

public class EmergencyContacts extends MenuActivity {

AppDatabase database;
String[] dataArray = {"Sample" , "John Doe", "Jane Doe"};
//Sample inputs for now. Will need to change later
String[] dataArray2;

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

    database = AppDatabase.getInstance(EmergencyContacts.this);
    //Contact test = new Contact("Mom", "12345");
    //database.getContactDao().insert(test);

    List<Contact> contacts = database.getContactDao().getAll();
    Log.d("contacts list", contacts.toString());

    ListView listView = (ListView)findViewById(R.id.contacts_listview);
    ArrayAdapter<Contact> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, contacts);
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
}