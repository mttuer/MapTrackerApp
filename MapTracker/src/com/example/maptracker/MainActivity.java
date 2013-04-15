package com.example.maptracker;

import interfaces.TrackerDB;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import util.Database;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.support.v4.app.FragmentActivity;


import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import dataWrappers.DBMarker;
import dataWrappers.GPS;
import dataWrappers.DBRoute;



public class MainActivity extends FragmentActivity implements OnMarkerClickListener{
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	
	public LinkedList<DBMarker> markerList = new LinkedList<DBMarker>();
	public LinkedList<GPS> gpsList = new LinkedList<GPS>();
	public HashMap<DBMarker, Marker> markerHashTable = new HashMap<DBMarker, Marker>();
	public long gpsTimeFreq = 5000;
	public int gpsMaxDistanceFreq = 500;
	public int gpsMinDistanceFreq = 10;
	public DBRoute thisRoute = new DBRoute();
	Database database = new Database(this);
	public LocationManager locationManager;
	public String provider;
	public LocationListener locListener;
	PolylineOptions lineOptions = null;
	public GoogleMap map;
	Polyline oldPolyline = null;
	LatLng lastPosition;
	Marker startMarker;
	Marker endMarker;

	
	Button export;
	Button edit;
	Button menu;
	Button handle, drawerMenu, drawerComment, drawerCamera, drawerMarker, drawerAudio, drawerVideo;
	SlidingDrawer sliding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_map);
		
		//Create the map Fragment
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        locListener = new LocationListener(){
        	
        	public void onLocationChanged(Location location)
        	{
				LatLng thisLocation = new LatLng(location.getLatitude(), location.getLongitude());
				double distance = 0;
				
				if(lastPosition != null)
				{
					distance = measure(thisLocation.latitude, thisLocation.longitude, lastPosition.latitude, lastPosition.longitude);
				}
				
				if(lastPosition == null)
				{
					startMarker = map.addMarker(new MarkerOptions()
			        .position(thisLocation)
			        .title("Starting marker"));
				}

				if(lastPosition == null || (distance < gpsMaxDistanceFreq && distance > gpsMinDistanceFreq))
				{
					//Create a new GPS object
					GPS gpsObject = new GPS();
					gpsObject.latitude = location.getLatitude();
					gpsObject.longitude = location.getLongitude();
					gpsObject.time = location.getTime();
					gpsList.add(gpsObject);
	        		
					System.out.println("Pass at Loc: " + thisLocation.latitude + ", " + thisLocation.longitude);

					if(lineOptions == null && oldPolyline == null)
					{
						lineOptions = new PolylineOptions();
						lineOptions.add(thisLocation);
					}
					else if (lineOptions != null && oldPolyline == null)
					{
						lineOptions.add(thisLocation);
						oldPolyline = map.addPolyline(lineOptions);
					}
					else
					{
						lineOptions.add(thisLocation);
						oldPolyline.remove();
						oldPolyline = map.addPolyline(lineOptions);
					}
					
					lastPosition = thisLocation;
				}		
            }
        	
        	public void onProviderDisabled(String provider) {}
        	public void onProviderEnabled(String provider) {}
        	public void onStatusChanged(String provider, int status, Bundle extras) {}
        };
        
		database.open();
        
        Location location = getLocation();
    	LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        long startTime = System.currentTimeMillis();
        
        for(int i = 0; i < 5; i++)
        {
        	//Create a new GPS object
			GPS gpsObject = new GPS();
			gpsObject.latitude = Math.random();
			gpsObject.longitude = Math.random();
			gpsObject.time = System.currentTimeMillis();
			gpsList.add(gpsObject);
        }
       
		database.addNewRoute(gpsList, markerList, startTime, System.currentTimeMillis(), "", "Test route", "Mikes House");

       
       List<DBRoute> routeList = database.getAllRoutes();
       System.out.println(routeList.size() + " Routes");
       for(DBRoute route : routeList)
       {
    	   System.out.println("Route: " + route.routeID);
    	   System.out.println("GPS Points: " + route.countDataPoints);
    	   System.out.println("Location Name: " + route.location);
    	   System.out.println("Route Name: " + route.routeName);
    	   System.out.println("Start Time: " + route.timeStart);
    	   System.out.println("End Time: " + route.timeEnd);
    	   
    	   List<GPS> gpsPoints = database.getGPSData(route.routeID);
    	   for(GPS gps : gpsPoints)
    	   {
    		   System.out.println("GPS: " + gps.latitude + ", " + gps.longitude + " at " + gps.time);
    	   }
       }
       
       handle = (Button) findViewById(R.id.handle);
		sliding = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		sliding.setOnDrawerOpenListener(new OnDrawerOpenListener() {
           @Override
           public void onDrawerOpened() {
               handle.setBackgroundResource(R.drawable.arrowclose);
           }
       });

       sliding.setOnDrawerCloseListener(new OnDrawerCloseListener() {
           @Override
           public void onDrawerClosed() {
               handle.setBackgroundResource(R.drawable.arrowopen);
           }
       });
       
       drawerMenu = (Button) findViewById(R.id.drawerButtonMenu);
		drawerMenu.setOnClickListener(new OnClickListener(){
		 public void onClick(View view) {
				Intent i = new Intent(view.getContext(), MenuActivity.class);
		        startActivity(i);
		 	}
		});
		
		drawerCamera = (Button) findViewById(R.id.drawerButtonCamera);
		drawerCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
			    // start the image capture Intent
			    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		drawerVideo = (Button) findViewById(R.id.drawerButtonVideo);
		drawerVideo.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				
			    // start the image capture Intent
			    startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
			}
		});
        
	}
	
	//Creates a marker on the map based on the current location and links it with the current marker
	public void createMarker(DBMarker marker)
	{
		//Call get location function
        Location location = getLocation();
        
        //If location is not null, create a marker at the location and draw a line from that location to lat:0 long:0
        if (location != null)
        {
        	//Get current location and create a LatLng object
        	LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        	Marker locationMarker = map.addMarker(new MarkerOptions().position(currentLocation));
        	
        	//Create a new GPS object to attach to the marker
        	GPS gpsObject = new GPS();
        	gpsObject.latitude = location.getLatitude();
        	gpsObject.longitude = location.getLongitude();
        	gpsObject.time = location.getTime();
        	marker.gps = gpsObject;
            
        	//Add the marker to the list of markers and HashTable
        	markerList.add(marker);
        	markerHashTable.put(marker, locationMarker);
        }
        else
        {
        	//Create alert when location fails
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Location not found.");
            builder.setTitle("Error");
            builder.setNeutralButton("Close", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
	}
	
	//Removes the marker from the map
	public void deleteMarker(DBMarker marker)
	{
		Marker locationMarker = markerHashTable.get(marker);
		locationMarker.remove();
	}

	//Get location every 3 seconds and save a GPS object to the GPS list.
	//Add a line to the new location
	public void startTracking()
	{
		thisRoute.timeStart = System.currentTimeMillis();
		
		locationManager.requestLocationUpdates(provider, gpsTimeFreq, 0, locListener);
	}
	
	//Stops tracking and saves the route data to the database
	public void stopTracking(String routeName, String notes, String location)
	{
		endMarker = map.addMarker(new MarkerOptions()
			        .position(new LatLng(getLocation().getLatitude(), getLocation().getLongitude()))
			        .title("Ending marker"));
		locationManager.removeUpdates(locListener);
		
		thisRoute.timeEnd = System.currentTimeMillis();
		thisRoute.routeName = routeName;
		thisRoute.notes = notes;
		thisRoute.location = location;
		
		database.addNewRoute(gpsList, markerList, thisRoute.timeStart, thisRoute.timeEnd, thisRoute.notes, thisRoute.routeName, thisRoute.location);
	}
	
	//Draws a line on the map for a given linked list of GPS objects
	public void drawPath(LinkedList<GPS> gpsList)
	{

		PolylineOptions lineOptions = new PolylineOptions();
		for(int i = 0; i < gpsList.size(); i++)
		{
			GPS currentLocation = gpsList.get(i);
			LatLng thisLocation = new LatLng(currentLocation.latitude, currentLocation.longitude);
			lineOptions.add(thisLocation);	
		}
    	
    	map.addPolyline(lineOptions);
   	}
	
	
	//Get location object
	public Location getLocation()
	{        
		    Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		    
		    long GPSLocationTime = 0;
		    if (locationGPS != null)
		    	GPSLocationTime = locationGPS.getTime();

		    long NetLocationTime = 0;
		    if (locationNet != null)
		        NetLocationTime = locationNet.getTime();
		    
		    if ( 0 < GPSLocationTime - NetLocationTime )
		        return locationGPS;
		    else
		        return locationNet;
	}

	//Fill in code here for what to do when Google Maps Marker is clicked
	//Check which DBMarker is connected with argument Marker and go from there
	public boolean onMarkerClick(Marker arg0) {
		
		if(arg0.equals(startMarker) || arg0.equals(endMarker))
			return false;
		
		Set<DBMarker> keyList = markerHashTable.keySet();
		DBMarker editedMarker = null;
		
		for(DBMarker marker : keyList)
		{
			if(markerHashTable.get(marker).equals(arg0))
			{
				//editedMarker = someFunction(markerHashTable.get(marker));
				markerHashTable.put(editedMarker, arg0);
				break;
			}
		}
		
		return true;
	}
	
	//Convert two lat/long coordinates to get the distance in meters
	public double measure(double lat1, double lon1, double lat2, double lon2){  // generally used geo measurement function
		double R = 6378.137; // Radius of earth in KM
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R * c;
	    return d * 1000; // meters
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Image saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }

	    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}

}
