package com.example.findmycar;

import javax.sql.DataSource;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindActivity extends FragmentActivity implements LocationListener{
	GoogleMap map;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapviewer);
		
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
	    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	        buildAlertMessageNoGps();
	    }
	    if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
	    map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        if (map == null) {
            Toast.makeText(this, "Google Maps not available", 
                Toast.LENGTH_LONG).show();
        }
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        	
        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            
        }else {	// Google Play Services are available	
		
			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			
			// Getting GoogleMap object from the fragment
			map = fm.getMap();
			
			// Enabling MyLocation Layer of Google Map
			map.setMyLocationEnabled(true);				
			
			 // Getting LocationManager object from System Service LOCATION_SERVICE
	        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	
	        // Creating a criteria object to retrieve provider
	        Criteria criteria = new Criteria();
	        // Getting the name of the best provider
	        String provider = locationManager.getBestProvider(criteria, true);
	        // Getting Current Location
	        Location location = locationManager.getLastKnownLocation(provider);
	        if(location!=null){
				double latitude = location.getLatitude();		
				// Getting longitude of the current location
				double longitude = location.getLongitude();	
				// Creating a LatLng object for the current location
				LatLng currentLoc = new LatLng(latitude, longitude);
				map.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));

				// Zoom in the Google Map
				map.animateCamera(CameraUpdateFactory.zoomTo(15));	
	                onLocationChanged(location);
	    	    locationManager.requestLocationUpdates(provider, 1000, 0, this);
	    	    buildAlertMessageDirectionToLocation(latitude, longitude);
	         }
	        else
	        {
	        	Toast.makeText(this, "Current Location cannot be obtained, please try again after your location has been set by Google Maps app", Toast.LENGTH_LONG).show();
	        }
        	
	    	}
	    }
	}
	
	private void buildAlertMessageNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
	
	private void buildAlertMessageDirectionToLocation(final double latitude,final double longitude) {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Do you want to go to Maps for direction?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	            	   String startLng, startLat;
	            	   String endLng, endLat;
	            	   MainActivity.parkedLocation = MainActivity.datasource.getParkedLocation(1);
	            	   LatLng parkedLoc = new LatLng (MainActivity.parkedLocation.getLattitude(),MainActivity.parkedLocation.getLongitude());
	            	   double startlat = latitude;
	            	   double startlng = longitude;
	            	   double endlat = parkedLoc.latitude;
	            	   double endlng = parkedLoc.longitude;
	            	   startLng = Double.toString(startlng);
	            	   startLat = Double.toString(startlat);
	            	   endLng = Double.toString(endlng);
	            	   endLat = Double.toString(endlat);
	            	   String uri = "http://maps.google.com/maps?saddr="+startLat + "," + startLng +"&daddr="+ endLat + "," + endLng;
	            	   Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
	            	   intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
	            	   startActivity(intent);	
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	            	    MainActivity.parkedLocation = MainActivity.datasource.getParkedLocation(1);
	            	    LatLng parkedLoc = new LatLng (MainActivity.parkedLocation.getLattitude(),MainActivity.parkedLocation.getLongitude());
	   					map.clear();
	            	    map.moveCamera(CameraUpdateFactory.newLatLng(parkedLoc));
	   					// Zoom in the Google Map
	   					map.animateCamera(CameraUpdateFactory.zoomTo(15));
	   					map.addMarker(new MarkerOptions()
		                .position(parkedLoc)
		                .title("Parked Location")
		                .snippet("Your approximate parked location")
		                .icon(BitmapDescriptorFactory
		                .fromResource(R.drawable.ic_launcher)));
	   					
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
