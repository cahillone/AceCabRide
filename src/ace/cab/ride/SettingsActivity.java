package ace.cab.ride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends Activity {
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.activity_settings);
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