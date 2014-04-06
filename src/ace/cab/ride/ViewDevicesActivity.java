package ace.cab.ride;

import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewDevicesActivity extends Activity {
	private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	// Return Intent extra
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	
	// Member fields
	private BluetoothAdapter mAdapter;
	private Handler mHandler;
	//private ConnectThread mConnectThread;
	//private ConnectedThread mConnectedThread;
	private int mState;
	
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		
		// Setup the window
	     requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	     setContentView(R.layout.device_list);
	     
	     // Set result CANCELED incase the user backs out
	     setResult(Activity.RESULT_CANCELED);
	     
	     // Initialize the button to perform device discovery
	     Button scanButton = (Button) findViewById(R.id.button_scan);
	     scanButton.setOnClickListener(new OnClickListener() {
	    	 public void onClick(View v) {
	    		 // doDiscovery();
	    		 v.setVisibility(View.GONE);
	    	 }
	     });
		
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
		
		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);
		
		// Get the local Bluetooth adapter
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		
		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
		
		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			mPairedDevicesArrayAdapter.add("No paired devices...");
		}
	}
	
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			// Cancel discovery because it's costly and we're about to connect
			mAdapter.cancelDiscovery();
			
			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);
			
			// Create the result Intent and include the MAC address
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
			
			// Set result and finish this Activity
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};
}
