package ace.cab.ride;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mDevice;
	private ConnectThread mConnectThread;
	private TextView tvConnectionStatus;
	private TextView tvLocationText;
	private TextView tvBAC;
	
	private static final String EXTRA_MESSAGE = null;
	private int REQUEST_ENABLE_BT = 1;
	
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_DEVICE_NAME = 1;
	
	public static final String DEVICE_NAME = "device_name";
	
	private String mConnectedDeviceName = null;
	private byte[] mByteArray = null;
	
	private boolean taxi_requested = false; // flag to indicate SMS message has been sent to taxi
	
	public void SetConnectedThread(){
		
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvConnectionStatus = (TextView)findViewById(R.id.bt_connection_status);
        tvConnectionStatus.setText("Breathalyzer is disconnected");
        
        tvLocationText = (TextView)findViewById(R.id.location_text);
        tvLocationText.setText("Location: not yet determined");
        
        tvBAC = (TextView)findViewById(R.id.BAC);
        tvBAC.setText("Your BAC: not yet determined");
    }
    
    private final Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		Log.i("TAG", "handler constructor: " + mHandler);
    		switch (msg.what){
    		case MESSAGE_DEVICE_NAME:
    			// save the connected device's name
    			mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
    			Log.i("TAG", "connected to: " + mConnectedDeviceName); // for debugging
    			
                tvConnectionStatus.setText("Breathalyzer connected via " + mConnectedDeviceName);
    			
                Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
    			break;
    		case MESSAGE_READ:
    			mByteArray = (byte[]) msg.obj;
    			if (mByteArray[0] < 10) {
    				tvBAC.setText("Your BAC: 0.0" + mByteArray[0]);
    			}
    			if (mByteArray[0] >= 10) {
    				tvBAC.setText("Your BAC: 0." + mByteArray[0]);
    			}
    			break;
    		}
    	}
    };

    // called when user presses taxi button
    public void SendTaxiSMS(View view) {
  
    		try {
    		DeviceLocation deviceLocation = new DeviceLocation(this); 
    		Location location = deviceLocation.getLocation();
    		String locationString = deviceLocation.displayLocation(location);
    		
            tvLocationText.setText("Location: " + locationString);
            
            // only request a taxi if one has not already been requested
            if (taxi_requested == true) {
            	Toast.makeText(getApplicationContext(), "Taxi has already been requested", Toast.LENGTH_SHORT).show();
            	return;
            }
            String SMSstring = "Driver,\n " +
            		"Will you pick me up at:\n" + 
            		locationString + "?\n" +
            		"Please respond to this number with the following format:\n" +
            		"yes/no approximate time (minutes)\n"; /* +
            		"example (yes I will be there in 10 minutes)\n" +
            		"y 10"; */
    		
  	
    		Intent intent = getIntent();
    		String strTaxiNumber = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(strTaxiNumber, null, SMSstring, null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_SHORT).show();
			taxi_requested = true;
		}catch (Exception e) {
			Toast.makeText(getApplicationContext(), "SMS Failed", Toast.LENGTH_SHORT).show();
			Log.i("TAG", "Error requesting taxi: " + e);
		}
    	
    };
    
    public void connect_BAC(View view){
    	// called when user presses Connect to Breathalyzer button
    	
    	// do nothing if already connected
    	if (mConnectThread != null) {
    		return;
    	}
    	
    	// ensure device supports Bluetooth
    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {
    	    // Device does not support Bluetooth
    		return;
    	}
    	// ensure Bluetooth is enabled
    	if (!mBluetoothAdapter.isEnabled()) {
    		// request user to enable Bluetooth
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    	}
    	
    	// set device address equal to MAC address of Bluetooth device on Breathalyzer
    	mDevice = mBluetoothAdapter.getRemoteDevice("20:13:11:14:01:49");
    	
    	// connect in a separate thread
    	if (mConnectThread != null) {
    		mConnectThread.cancel();
    	}
    	mConnectThread = new ConnectThread(mDevice, mBluetoothAdapter, mHandler);
    	mConnectThread.start();
    };
    
    @Override
    public void onStop() {
    	// disconnect Bluetooth when app is closed
    	super.onStop();
    	if (mConnectThread != null) {
    		mConnectThread.cancel();
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection
    	switch (item.getItemId()) {
    	case R.id.action_settings:
    		// go to settings activity
    		Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
    		startActivity(settingsIntent);
    		return true;
    	case R.id.action_main:
    		// go to main activity
    		Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
    		startActivity(mainIntent);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
