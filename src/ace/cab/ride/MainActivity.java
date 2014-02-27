package ace.cab.ride;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
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
        
        try {
		DeviceLocation deviceLocation = new DeviceLocation(this); 
        Location location = deviceLocation.getLocation();
        String coordinates = deviceLocation.displayLocation(location);
        
        TextView locationText = (TextView)findViewById(R.id.location_text);
        
        locationText.setText(coordinates);
        
        } catch (Exception e) {
        	TextView locationText = (TextView)findViewById(R.id.location_text);
            
            locationText.setText("unable to determine device location");
        }
        
        requestTaxi = (Button)findViewById(R.id.taxi_button);
        requestTaxi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				TextView locationtext = (TextView)findViewById(R.id.location_text);
				String myString = locationtext.getText().toString();
				
				Intent intent = getIntent();
			    String strTaxiNumber = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

				
				try {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(strTaxiNumber, null, myString + "\nfrom my app", null, null);
					Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_LONG).show();
				}catch (Exception e) {
					Toast.makeText(getApplicationContext(), "SMS Failed", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});
        
		        
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
