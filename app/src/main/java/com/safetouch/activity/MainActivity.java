package com.safetouch.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.safetouch.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends MenuActivity implements View.OnClickListener {

    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    SmsManager smsManager = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {

    }

    public void sendSMSEmergencyText(View view) {
        // TODO: Get contact phone numbers and message from database
        List<String> phoneNumbers = Arrays.asList("123",  "456", "789");
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                getPermissionToReadSMS();
            } else {
                // NOTE: any phone number can go here, the text will get sent to the emulator
                // Loop through phoneNumbers array and send text to each one
                for (String number : phoneNumbers) {
                    String message = "Hello from SafeTouch! " + number;
                    smsManager.sendTextMessage(number, null, message, null, null);
                }
                Toast.makeText(this, phoneNumbers.size() != 1 ? "Messages sent!" : "Message sent!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Allow SMS send permissions", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
