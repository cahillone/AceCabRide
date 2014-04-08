package ace.cab.ride;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PairedListActivity extends Activity {
	private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	BluetoothAdapter mBluetoothAdapter;
	ConnectThread mConnectThread;
	BluetoothDevice mDevice;
	private int REQUEST_ENABLE_BT = 1;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_name_address);
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();		
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			ArrayAdapter <String> mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView		        
		    	mArrayAdapter.add(device.getName() + "\n" + device.getAddress());	
		    	mDevice = device;
		    }
	        ListView pairedDevicesListView = (ListView) findViewById(R.id.btName);
	        pairedDevicesListView.setAdapter(mArrayAdapter);
		}
		
		// Start thread to connect with the given device
		mConnectThread = new ConnectThread(mDevice);
		mConnectThread.run();
		
	}
	
	private class ConnectThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	        Toast.makeText(getApplicationContext(), "canceling discovery", Toast.LENGTH_SHORT).show();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	            Toast.makeText(getApplicationContext(), "connecting...", Toast.LENGTH_SHORT).show();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	        	Toast.makeText(getApplicationContext(), "unable to connect", Toast.LENGTH_SHORT).show();
	            try {
	                mmSocket.close();
	                Toast.makeText(getApplicationContext(), "socket closed", Toast.LENGTH_SHORT).show();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        // manageConnectedSocket(mmSocket);
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
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
    	case R.id.action_connect:
    		// go to BT paired devices list
    		Intent pairedListActivity = new Intent(getApplicationContext(), PairedListActivity.class);
        	startActivity(pairedListActivity);
    	default:
    		return super.onOptionsItemSelected(item);
    	}
	}
}
