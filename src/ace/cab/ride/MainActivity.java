package ace.cab.ride;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mDevice;
	private ConnectThread mConnectThread;
	
	private static final String EXTRA_MESSAGE = null;
	private int REQUEST_ENABLE_BT = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_DEVICE_NAME = 1;
	public static final String DEVICE_NAME = "device_name";
	Button requestTaxi;
	TextView RxBuffer;
	
	private String mConnectedDeviceName = null;
	private byte[] mByteArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        
        /*
        RxBuffer = new TextView();
        
        RxBuffer = (TextView) findViewById(R.id.read_value);
        
        mHandler = new Handler(Looper.getMainLooper()) {
        	@Override
        	public void handleMessage(Message inputMessage) {
        		switch(inputMessage.what){
        		case MESSAGE_READ:
        			byte[] readBuf = (byte[]) inputMessage.obj;
        			String str = new String(readBuf, 0, inputMessage.arg1);
        			Log.i("TAG", "in case MESSAGE_READ main activity");
        			Log.i("TAG", "string in Main Activity: " + str);
        			Log.i("TAG", "readBuf[0] in Main Activity: " + readBuf[0]);
        			Log.i("TAG", "readBuf[1] in Main Activity: " + readBuf[1]);
        			Log.i("TAG", "readBuf[2] in Main Activity: " + readBuf[2]);
        			Log.i("TAG", "readBuf[3] in Main Activity: " + readBuf[3]);
        			break;
        		default:
        			super.handleMessage(inputMessage);
        		}
        	}
        };
        */
        Log.i("TAG", "handler onCreate: " + mHandler);
        
    }
    
    private final Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		Log.i("TAG", "handler constructor: " + mHandler);
    		switch (msg.what){
    		case MESSAGE_DEVICE_NAME:
    			// save the connected device's name
    			mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
    			Log.i("TAG", "connected to: " + mConnectedDeviceName);
    			Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
    			break;
    		case MESSAGE_READ:
    			mByteArray = (byte[]) msg.obj;
    			Log.i("TAG", "mByteArray[0] in Main Activity: " + mByteArray[0]);
    			Log.i("TAG", "mByteArray[1] in Main Activity: " + mByteArray[1]);
    			Log.i("TAG", "mByteArray[2] in Main Activity: " + mByteArray[2]);
    			Log.i("TAG", "mByteArray[3] in Main Activity: " + mByteArray[3]);
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
            TextView locationText = (TextView)findViewById(R.id.location_text);
            
            locationText.setText(locationString);
            
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
			Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_LONG).show();
		}catch (Exception e) {
			Toast.makeText(getApplicationContext(), "SMS Failed", Toast.LENGTH_LONG).show();
			//e.printStackTrace();
		}
    	
    };
    
    public void fetchBAC(View view){
    	// called when user presses BAC button (Connect to Breathalyzer button)
    	// ensure device supports Bluetooth
    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {
    	    // Device does not support Bluetooth
    		return;
    	}
    	// ensure Bluetooth is enabled
    	if (!mBluetoothAdapter.isEnabled()) {
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    	}
    	// set device address equal to MAC address of Bluetooth device on Breathalyzer
    	mDevice = mBluetoothAdapter.getRemoteDevice("20:13:11:14:01:49");
    	// connect in a separate thread
    	if (mConnectThread != null) {
    		mConnectThread.cancel();
    	}
    	
    	Log.i("TAG", "handler fetchBAC: " + mHandler);
    	mConnectThread = new ConnectThread(mDevice, mBluetoothAdapter, mHandler);
    	mConnectThread.start();
    	
    	Log.i("TAG", "reached end of fetchBAC()");
    	
    	// return; // ????
    };
    
    @Override
    public void onStop() {
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
