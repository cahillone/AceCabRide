package ace.cab.ride;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class BacActivity extends Activity {
	private int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayAdapter<String> mArrayAdapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bac);   
        
        setUpBluetooth(mBluetoothAdapter);
        queryPairedDevices(mBluetoothAdapter);
    }
	
	private void setUpBluetooth(BluetoothAdapter mBluetoothAdapter) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}
	
	private void queryPairedDevices(BluetoothAdapter mBluetoothAdapter) {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	mArrayAdapter = new ArrayAdapter<String>(this, R.layout.bt_name_address);
		        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		        
		        /* not sure here...
		        ListView pairedDevicesListView = (ListView) findViewById(R.id.tvName);
		       	
		        
		        
		        pairedDevicesListView.setAdapter(mArrayAdapter);
		    	*/
		    }
		}
	}
}