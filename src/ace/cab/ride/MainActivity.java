package ace.cab.ride;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String EXTRA_MESSAGE = null;
	Button requestTaxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
    }
    
    // called when user presses taxi button
    public void SendTaxiSMS(View view) {
    	
    		/*
    		Intent taxiIntent = new Intent(getApplicationContext(), TaxiActivity.class);
    		startActivity(taxiIntent);
    		*/
    		
    		
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
    	// called when user presses BAC button
    	
    	// go to BAC activity
    	Intent BACIntent = new Intent(getApplicationContext(), BacActivity.class);
    	startActivity(BACIntent);
    	
    };
    
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
