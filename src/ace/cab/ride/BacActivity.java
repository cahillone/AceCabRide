package ace.cab.ride;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BacActivity extends Activity {
	
	private final static int REQUEST_ENABLE_BT = 1;
	//private ArrayAdapter <String> mArrayAdapter;
	//private BluetoothSocket Socket;
	private static final String TAG = "bluetooth1";
	
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	
	// SPP UUID Service
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	// MAC address of Bluetooth module (hardcoded)
	private static String address = "20:13:11:14:01:49";
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.activity_bac);
	     
			Toast.makeText(getApplicationContext(), "start of onCreate", Toast.LENGTH_LONG).show();

	     
	     btAdapter = BluetoothAdapter.getDefaultAdapter();
	     
			Toast.makeText(getApplicationContext(), "mid onCreate", Toast.LENGTH_LONG).show();
	     
	     checkBTState();
	     
			Toast.makeText(getApplicationContext(), "end onCreate", Toast.LENGTH_LONG).show();

	}
	
	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
		if(Build.VERSION.SDK_INT >= 10) {
			try {
				final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
				return (BluetoothSocket) m.invoke(device, MY_UUID);
			} catch (Exception e) {
				Log.e(TAG, "Could not create Insecure RFComm Connection", e);
			}
		}
		return device.createRfcommSocketToServiceRecord(MY_UUID);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Toast.makeText(getApplicationContext(), "onResume...", Toast.LENGTH_LONG).show();

		
		Log.d(TAG, "... onResume - try connect...");
		
		// Set up a pointer to the remote node using it's address
		BluetoothDevice device = btAdapter.getRemoteDevice(address);
		
		try {
			btSocket = createBluetoothSocket(device);
			
			Toast.makeText(getApplicationContext(), "socket created", Toast.LENGTH_LONG).show();

			
		} catch (IOException e1) {
			errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
		}
		
		// Discovery is resource intensive. Make sure it isn't going on
		// when you attemp to connect and pass your message.
		btAdapter.cancelDiscovery();
		
		// Establish the connection. This will block until it connects.
		Log.d(TAG, "...Connecting...");
		try {
			btSocket.connect();
			Log.d(TAG, "...Connection ok...");
			
			Toast.makeText(getApplicationContext(), "Bluetooth connected", Toast.LENGTH_LONG).show();

			
		} catch (IOException e) {
			try {
				btSocket.close();
				
				Toast.makeText(getApplicationContext(), "socket closed", Toast.LENGTH_LONG).show();

				
			} catch (IOException e2) {
				errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
			}
		}
		
		// Create a data stream so we can talk to server.
		Log.d(TAG, "...Create Socket...");
		
		try {
			outStream = btSocket.getOutputStream();
		} catch (IOException e) {
			errorExit("Fatal Error", "In onResume() and output stream creation failed: " + e.getMessage() + ".");
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		Log.d(TAG, "...In onPause()...");
		
		if (outStream != null) {
			try {
				outStream.flush();
			} catch (IOException e) {
				errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
			}
		}
		
		try {
			btSocket.close();
		} catch (IOException e2) {
			errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
		}
	}
	
	private void checkBTState() {
		// make sure device supports Bluetooth
		// make sure device's Bluetooth is turned on
		
		if (btAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(getApplicationContext(), "Device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
		} else {
		
			if (!btAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			} else {
				Toast.makeText(getApplicationContext(), "Bluetooth is ON", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void errorExit(String title, String message) {
		Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
		finish();
	}
	     
	     /*
	     BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	 	
			if (mBluetoothAdapter == null) {
				// Device does not support Bluetooth
				Toast.makeText(getApplicationContext(), "Device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
			} else {
			
				if (!mBluetoothAdapter.isEnabled()) {
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}
				
				
				BluetoothDevice breathalyzer;
				BluetoothSocket Socket;
				
				breathalyzer = BluetoothAdapter.getDefaultAdapter().getRemoteDevice("20:13:11:14:01:49");
				
				try {
					Socket.connect();
				} catch (IOException connectException) {
					// Unable to connect; close the socket and get out
					try {
						Socket.close();
					} catch (IOException closeException) {}
					return;
							
				}
				
				
				/*
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				// If there are paired devices
				if (pairedDevices.size() > 0) {
					// Loop through paired devices
					for (BluetoothDevice device : pairedDevices) {
						// Add the name and address to an array adapter to show in a ListView
						
						mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
					}
				}
				*/
				
				/*
				private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
					public void onReceive(Context context, Intent intent) {
						String action = intent.getAction();
						// When discovery finds a device
						if (BluetoothDevice.ACTION_FOUND.equals(action)) {
							// Get the BluetoothDevice object from the Intent
							BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
							// Add the name and address to an array adapter to show in a ListView
							mDiscArrayAdapter.add(device.getName() + "\n" + device.getAddress());
						}
					}
				};
				// Register the BroadcastReceiver
				IntentFilter filter = new Intent Filter(BluetoothDevice.ACTION_FOUND);
				registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
				
			}
	 }
	 */
	
	/*
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
				// MY_UUID is the app's UUID string
				tmp = device.createRfcommSocketToServiceRecord("1101");
			} catch (IOException e) {} 
			mmSocket = tmp;
		}
	
		public void run() {
			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDisovery();
		
			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				mmSocket.connect();
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) { }
				return;
			}
		
			// Do work to manage the connection (in a separate thread)
			manageConnectedSocket(mmSocket);
		}
		
		// Will cancel an in-progress connection, and close the socket
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) { }
		}
	}
	*/
		 
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
