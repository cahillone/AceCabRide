package ace.cab.ride;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

public class DeviceLocation {
	Context locationContext;
	
	public DeviceLocation(Context locationContext) {
		this.locationContext = locationContext;
	}
	
	//Geocoder geocoder = new Geocoder(locationContext);
	
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
		String address;
		List<Address> addressList = new ArrayList<Address>();
		
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		
			latAndLong = "Latitude: " + latitude + "\nlongitude: " + longitude;
			
			Geocoder geocoder = new Geocoder(locationContext);
			
			try {
			
			addressList = geocoder.getFromLocation(latitude, longitude, 1);
			
			}
			catch (Exception e) {
				return "unable to convert lat and long to address";
			}
			
			address = addressList.get(0).toString();
			
			if (address == null || addressList.get(0) == null) {
				return "no address matches were found or there is no backend service available";
			}
			
			return address;
		}else{
			return "unable to determine location";
		}
		
	}

}
