package com.safetouch.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.safetouch.R;
import com.safetouch.database.AppDatabase;
import com.safetouch.domain.Contact;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ParentActivity extends MenuActivity {

    private FusedLocationProviderClient client;
    SmsManager smsManager = SmsManager.getDefault();

    private String userAddress;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        database = AppDatabase.getInstance(ParentActivity.this);

        Button sendEmergencyText = (Button) findViewById(R.id.sendtext);
        Button sendFalseAlarm = (Button) findViewById(R.id.falsealarm);

        client = LocationServices.getFusedLocationProviderClient(this);

        // Emergency Text
        sendEmergencyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMSEmergencyText();
            }
        });

        // False Alarm Text
        sendFalseAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFalseAlarmText();
            }
        });
    }

    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //testing to see if the useraddress is null
            sendLocation();
            Toast.makeText(this, "Allow SMS send permissions\n"+userAddress, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMSEmergencyText() {
        List<Contact> contacts = database.getContactDao().getAll();
        //String emergencyMessage = database.getConfigurationDao().getEmergencyMessage();
        String emergencyMessage = "";
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                getPermissionToReadSMS();
            } else {
                emergencyMessage=getDefaults("preset_msg",getApplicationContext());//get msg from settings
                // NOTE: any phone number can go here, the text will get sent to the emulator
                // Loop through phoneNumbers array and send text to each one
                sendLocation();
                String location = userAddress;
                for (Contact contact : contacts) {
                    String message = "From SafeTouch: " + emergencyMessage + " Current Location: " + location;
                    smsManager.sendTextMessage(contact.getPhoneNumber(), null, message, null, null);
                }
                Toast.makeText(this, contacts.size() != 1 ? "Messages sent!" : "Message sent!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void sendFalseAlarmText() {
        List<Contact> contacts = database.getContactDao().getAll();
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                getPermissionToReadSMS();
            } else {
                for (Contact contact : contacts) {
                    String message = "From SafeTouch: The last text was a result of a unintentional button press. Please ignore.";
                    smsManager.sendTextMessage(contact.getPhoneNumber(), null, message, null, null);
                }
                Toast.makeText(this, contacts.size() != 1 ? "Messages sent!" : "Message sent!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sendLocation() {
        if (ActivityCompat.checkSelfPermission(ParentActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(ParentActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    LatLng ll = new LatLng(lat,lon);
                    //Toast.makeText(getApplicationContext(), "lat:" + lat + "lon:" + lon, Toast.LENGTH_SHORT).show();
                    userAddress = getAddress(getApplicationContext(),ll.latitude,ll.longitude);
                    //Toast.makeText(getApplicationContext(), userAddress, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getAddress(Context ctx, double lat, double lon) {
        String address = "";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, lon, 1);

            if (listAddresses != null && listAddresses.size() > 0) {
                Log.i("PlaceInfo", listAddresses.get(0).toString());

                if (listAddresses.get(0).getSubThoroughfare() != null) {
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }
                if (listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + ", ";
                }
                if (listAddresses.get(0).getLocality() != null) {
                    address += listAddresses.get(0).getLocality() + ", ";
                }
                if (listAddresses.get(0).getPostalCode() != null) {
                    address += listAddresses.get(0).getPostalCode() + ", ";
                }
                if (listAddresses.get(0).getCountryName() != null) {
                    address += listAddresses.get(0).getCountryName();
                }
                Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}