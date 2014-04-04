package ace.cab.ride;

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
	
	//@input location
	//@ret String of human readable device location
	// converts latitude and longitude into a human readable location using the geocode API
	public String displayLocation(Location location){
		if (location != null) {
			Geocoder geocoder = new Geocoder(locationContext);
			
			try {
				List<Address> addressList = geocoder.getFromLocation(
						location.getLatitude(), 
						location.getLongitude(), 
						1);
				
				//List<Address> addressList = geocoder.getFromLocation(39.729567, -121.833411, 1);
				
				if (addressList != null) {
					Address address = addressList.get(0);
					String stringAddress = String.format(
	                        "%s, %s",
	                        // If there's a street address, add it
	                        address.getMaxAddressLineIndex() > 0 ?
	                                address.getAddressLine(0) : "",
	                        // Locality is usually a city
	                        address.getLocality());
	                // Return the text
					return stringAddress;
				}
				else {
					return "no address matches found or no backend service available";
				}			
			}
			catch (Exception e) {
				return "unable to convert latitude and longitude to address";
			}
		}else{
			return "unable to determine location\n no Location";
		}
	}
}
