package ace.cab.ride;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

public class TaxiActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);
    }
}
