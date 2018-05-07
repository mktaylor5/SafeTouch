package com.safetouch.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MedicalActivity extends MenuActivity {

    private FusedLocationProviderClient client;
    SmsManager smsManager = SmsManager.getDefault();

    private Handler btHandler; // Our main handler that will receive callback notifications
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    private BluetoothAdapter btAdapter = null;
    private ConnectedThread btConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket btSocket = null; // bi-directional client-to-client data path

    private final String TAG = MainActivity.class.getSimpleName();
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    SharedPreferences preferences = null;
    boolean alarmOn, awaitingCheckIn = false, btConnected = false;

    CountDownTimer checkin;

    private String userAddress;
    AppDatabase database;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
        database = AppDatabase.getInstance(MedicalActivity.this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        alarmOn = preferences.getBoolean("alarmOnOff", false);

        Button sendEmergencyText = (Button) findViewById(R.id.sendtext);
        Button sendFalseAlarm = (Button) findViewById(R.id.falsealarm);

        // Bluetooth
        if(btConnected == false) {
            establishBluetoothConnection();
        }
        btHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                //Log.d(msg.obj.toString(), "msg");
                if (msg.what == MESSAGE_READ) {
                    String readMessage;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                        Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_LONG).show();
                        if (readMessage.toLowerCase().contains("single"))
                        {
                            if (awaitingCheckIn) {
                                checkin.cancel();
                                awaitingCheckIn = false;
                            }
                            else {
                                // Sends text and location information
                                sendSMSEmergencyText();
                                //mMessageSender.run();
                                btConnectedThread.write(alarmOn ? "On" : "Off");
                            }
                        }
                        else if (readMessage.toLowerCase().contains("double"))
                        {
                            // TODO: do something?
                            Toast.makeText(getApplicationContext(), "Double tap detected", Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1) {
                        //btStatus.setText("Connected to Device: " + (String) (msg.obj));
                        Toast.makeText(getApplicationContext(), "Connected:" + (String) msg.obj, Toast.LENGTH_LONG).show();
                        btConnected=true;
                    }
                    else {
                        //btStatus.setText("Connection Failed");
                        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                        btConnected=false;
                        establishBluetoothConnection();
                    }

                }
            }
        };

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

    public void checkInDone() {
        awaitingCheckIn = true;
        checkin = new CountDownTimer(5000, 1000) { // 5 second timer
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                // check in time not met, send text
                sendSMSEmergencyText();
                awaitingCheckIn = false;
                //finish();
            }
        }.start();
    }

    private final Runnable mMessageSender = new Runnable() {
        public void run() {
            Message msg = btHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("alarm", alarmOn ? "On" : "Off");
            msg.setData(bundle);
            btHandler.sendMessage(msg);
        }
    };

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
                String location = sendLocation();
                if (location == null) {
                    location = sendLocation();
                    if (location == null) {
                        location = "No location found.";
                    }
                }
                Log.i("location", location);
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

    private String sendLocation() {
        if (ActivityCompat.checkSelfPermission(MedicalActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        client.getLastLocation().addOnSuccessListener(MedicalActivity.this, new OnSuccessListener<Location>() {
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
        return userAddress;
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

    public void establishBluetoothConnection() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            //Show a message that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth not available on this device.", Toast.LENGTH_LONG).show();
            //finish();
        } else {
            if (btAdapter.isEnabled()) {
                new Thread() {
                    public void run() {
                        boolean fail = false;
                        String address = "";

                        Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
                        for(BluetoothDevice device: devices){
                            if(device.getName().equals("HC-05")){
                                address = device.getAddress();
                            }
                        }
                        BluetoothDevice device = btAdapter.getRemoteDevice(address);

                        try {
                            btSocket = createBluetoothSocket(device);
                        } catch (IOException e) {
                            fail = true;
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                        // Establish the Bluetooth socket connection.
                        try {
                            btSocket.connect();
                        } catch (IOException e) {
                            try {
                                fail = true;
                                btSocket.close();
                                btHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                        .sendToTarget();
                            } catch (IOException e2) {
                                //insert code to deal with this
                                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (fail == false) {
                            btConnectedThread = new ConnectedThread(btSocket);
                            btConnectedThread.start();
                            btHandler.obtainMessage(CONNECTING_STATUS, 1, -1, device.getName()).sendToTarget();
                        }
                    }
                }.start();
            } else {
                //Ask to the user turn the bluetooth on
                //Toast.makeText(this, "Bluetooth device not available", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create RFComm Connection", e);
        }
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        btHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}