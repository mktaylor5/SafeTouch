package com.safetouch.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.safetouch.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends MenuActivity implements View.OnClickListener {
    private FusedLocationProviderClient client;
    private Button button;
    private String userAddress;


    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    SmsManager smsManager = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);
        // TextView myAddress = (TextView)findViewById(R.id.myaddress);
        button = (Button)findViewById(R.id.send_location) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!= null){
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            Toast.makeText(getApplicationContext(),"lat:"+lat+"lon:"+lon,Toast.LENGTH_SHORT).show();
                            userAddress=getAddress(getApplicationContext(),lat,lon);
                            Toast.makeText(getApplicationContext(),userAddress,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }

    private String getAddress(Context ctx,double lat,double lon){
        String address = "";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat,lon, 1);

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
                // do nothing
                //Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
