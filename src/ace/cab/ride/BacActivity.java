package ace.cab.ride;

import java.util.ArrayList;
import java.util.List;
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


public class BacActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bac);   
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