package com.safetouch.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.safetouch.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
    private BluetoothAdapter btAdapter = null;
    private Handler btHandler; // Our main handler that will receive callback notifications
    private BluetoothActivity.ConnectedThread btConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket btSocket = null; // bi-directional client-to-client data path

    private final String TAG = MainActivity.class.getSimpleName();
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;


    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestLocationPermission();
        //database = AppDatabase.getInstance(MainActivity.this);

        // Bluetooth
        establishBluetoothConnection();
        btHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                //Log.d(msg.obj.toString(), "msg");
                if (msg.what == MESSAGE_READ) {
                    String readMessage;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                        //Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_LONG).show();
//                        if (readMessage != null)
//                        {
//                            // Sends text and location information
//                            sendSMSEmergencyText();
//                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1)
                        //btStatus.setText("Connected to Device: " + (String) (msg.obj));
                        Toast.makeText(getApplicationContext(), "Connected:" + (String)msg.obj, Toast.LENGTH_LONG).show();
                    else
                        //btStatus.setText("Connection Failed");
                        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                }
            }
        };
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
            } catch (IOException e) {
            }

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
                    if (bytes != 0) {
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
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}