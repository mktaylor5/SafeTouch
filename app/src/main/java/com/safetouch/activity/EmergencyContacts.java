package com.safetouch.activity;

/**
 * Created by Monica on 3/6/2018.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.safetouch.R;
import com.safetouch.database.AppDatabase;
import com.safetouch.domain.Contact;

import java.util.List;

public class EmergencyContacts extends MenuActivity {

    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts);
        getSupportActionBar();


        FloatingActionButton fab = findViewById(R.id.contacts_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//floating button pressed
                addNewContact();
            }
        });

        database = AppDatabase.getInstance(EmergencyContacts.this);

        final List<Contact> contacts = database.getContactDao().getAll();
        //Log.d("contacts list", contacts.toString());

        ListView listView = (ListView)findViewById(R.id.contacts_listview);
        ArrayAdapter<Contact> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, contacts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//item in list clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {//edit contact
                AlertDialog.Builder a_builder = new AlertDialog.Builder(EmergencyContacts.this);

                //layout for dialog
                LinearLayout layout = new LinearLayout(EmergencyContacts.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText nameInput = new EditText(EmergencyContacts.this);
                final EditText numberInput = new EditText(EmergencyContacts.this);

                nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                numberInput.setInputType(InputType.TYPE_CLASS_PHONE);

                nameInput.setText(contacts.get(position).getName(), TextView.BufferType.EDITABLE);
                numberInput.setText(contacts.get(position).getPhoneNumber(), TextView.BufferType.EDITABLE);

//                numberInput.addTextChangedListener(new TextWatcher() {
//                    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {/*DO NOTHING*/}
//                    @Override public void afterTextChanged(Editable editable) {/*DO NOTHING*/}
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        if (getCurrentFocus() == numberInput) {
//                            String phoneNumber = ((TextView)numberInput).getText().toString();
//                            if (!validatePhoneNumber(phoneNumber)) {
//                                numberInput.setError("Please enter a valid 10-digit phone number.");
//                            }
//                        }
//                    }
//                });

                layout.addView(nameInput);
                layout.addView(numberInput);
                a_builder.setView(layout);

                a_builder.setMessage("")
                        .setCancelable(false)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contact current = contacts.get(position);
                                database.getContactDao().updateContact(current.getContactID(), nameInput.getText().toString(),numberInput.getText().toString());
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNeutralButton("Delete Contact", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.getContactDao().deleteContact(contacts.get(position));
                                finish();
                                startActivity(getIntent());
                            }
                        });

                AlertDialog alert = a_builder.create();
                alert.setTitle("Edit Emergency Contact");
                alert.show();

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

    public void addNewContact(){//inserts into db
        AlertDialog.Builder a_builder = new AlertDialog.Builder(EmergencyContacts.this);

        //layout for dialog
        LinearLayout layout = new LinearLayout(EmergencyContacts.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText nameInput = new EditText(EmergencyContacts.this);
        final EditText numberInput = new EditText(EmergencyContacts.this);

        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        numberInput.setInputType(InputType.TYPE_CLASS_PHONE);

        nameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {//for text to disappear when input entered
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameInput.setHint("Enter Name");
            }
        });

        numberInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                numberInput.setHint("Enter Phone Number");
            }
        });

        nameInput.addTextChangedListener(new TextWatcher() {//gives error if no input
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(nameInput.getText().toString().length() == 0)
                    nameInput.setError("Name is required!");
            }
        });

        numberInput.addTextChangedListener(new TextWatcher() {//gives error if no input
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(numberInput.getText().toString().length() == 0)
                    numberInput.setError("Phone number is required!");
                if(!validatePhoneNumber(numberInput.getText().toString()))
                    numberInput.setError("Please enter a valid 10 digit phone number.");
            }
        });

        layout.addView(nameInput);
        layout.addView(numberInput);
        a_builder.setView(layout);

        a_builder.setMessage("")
                .setCancelable(false)
                .setPositiveButton("Create",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//insert
                        if(nameInput.getText().toString().length() != 0 && numberInput.getText().toString().length() != 0){//cannot be empty
                            if (validatePhoneNumber(numberInput.getText().toString())) {
                                Contact newContact = new Contact(nameInput.getText().toString(), numberInput.getText().toString());
                                database.getContactDao().insert(newContact);
                                database = AppDatabase.getInstance(EmergencyContacts.this);
                                finish();
                                startActivity(getIntent());
                            }
                            else {
                                // TODO: Make the dialog stay open if validation fails and submit is pressed
                                numberInput.setError("Please enter a valid 10 digit phone number.");
                                Toast.makeText(EmergencyContacts.this, "Phone number must contain 10 digits.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(EmergencyContacts.this, "Contact not saved. Name and phone number must contain valid values.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }) ;
        AlertDialog alert = a_builder.create();
        alert.setTitle("New Emergency Contact");
        alert.show();
    }

//    @Override
//    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable editable) {
//        String phoneNumber = ((TextView)numberInput).getText().toString();
//        if (!validatePhoneNumber(phoneNumber)) {
//            numberInput.setError("Please enter a valid 10-digit phone number.");
//        }
//    }

    public boolean validatePhoneNumber(String number) {
        boolean valid = true;
        String digits = number.replaceAll("[^0-9]", "");
        if (digits.length() != 10) {
            valid = false;
        }
        return valid;
    }
}