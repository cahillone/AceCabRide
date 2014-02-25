package ace.cab.ride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	 private static final String EXTRA_MESSAGE = null;

	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.activity_settings);
	 }
	 
	 // Called when the user presses the update taxi phone number button
	 public void UpdateTaxiPhone(View view) {
		 Intent intent = new Intent(this, MainActivity.class);
		    EditText editText = (EditText) findViewById(R.id.taxi_phone_number);
		    String message = editText.getText().toString();
		    intent.putExtra(EXTRA_MESSAGE, message);
		    startActivity(intent);


		 
		 /*
		 EditText editText = (EditText)getText(R.id.taxi_phone_number);
		 String taxiPhone = editText.toString();
		 editText.setText(taxiPhone);
		 */
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