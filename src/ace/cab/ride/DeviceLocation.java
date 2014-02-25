package ace.cab.ride;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class DeviceLocation {
	Context locationContext;
	
	public DeviceLocation(Context locationContext) {
		this.locationContext = locationContext;
	}
	
	public Location getLocation(){
		LocationManager locationManager;
		
		String context = Context.LOCATION_SERVICE;
		
		locationManager = (LocationManager)locationContext.getSystemService(context);
		
		String provider = LocationManager.NETWORK_PROVIDER;
		
		//String provider = LocationManager.GPS_PROVIDER;
		
		Location location = locationManager.getLastKnownLocation(provider);
		
		return location;
	}
	
	public String displayLocation(Location location){
		double latitude;
		double longitude;
		String latAndLong;
		
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		
			latAndLong = "Latitude: " + latitude + "\nlongitude: " + longitude;
		
			return latAndLong;
		}else{
			return "unable to determine location";
		}
		
	}

}
