package ace.cab.ride;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PairedListActivity extends Activity {
	private int REQUEST_ENABLE_BT = 1;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_name_address);
        
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
		    }
	        ListView pairedDevicesListView = (ListView) findViewById(R.id.btName);
	        pairedDevicesListView.setAdapter(mArrayAdapter);
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
