package ace.cab.ride;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
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
	private Handler mHandler;
	
	private static final String EXTRA_MESSAGE = null;
	private int REQUEST_ENABLE_BT = 1;
	final int RECEIVE_MESSAGE = 1;
	Button requestTaxi;
	TextView RxBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        RxBuffer = (TextView) findViewById(R.id.read_value);
        
        
        mHandler = new Handler() {
        	public void handleMessage(android.os.Message msg) {
        		switch (msg.what){
        		case RECEIVE_MESSAGE:
        			byte[] readBuf = (byte[]) msg.obj;
        			String strIncom = new String(readBuf, 0, msg.arg1);
        			RxBuffer.setText("Rx Buffer: " + strIncom);
        			break;
        			/*
        			mStringBuilder.append(strIncom);
        			int endOfLineIndex = mStringBuilder.indexOf("\r\n");
        			if (endOfLineIndex > 0) {
        				String sbprint = mStringBuilder.substring(0, endOfLineIndex);
        				mStringBuilder.delete(0, sb.length());
        				RxBuffer.setText("Rx Buffer: " + sbprint);
        			}
        			*/
        		}
        	}
        };
    }
    
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
    	mConnectThread = new ConnectThread(mDevice);
    	mConnectThread.run(mBluetoothAdapter);
    	
    	return; // ????
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
