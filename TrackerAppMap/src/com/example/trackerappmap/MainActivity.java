package com.example.trackerappmap;

import interfaces.TrackerDB;

import java.util.HashMap;
import java.util.LinkedList;

import util.Database;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import dataWrappers.DBMarker;
import dataWrappers.GPS;
import dataWrappers.Route;



public class MainActivity extends FragmentActivity implements OnMarkerClickListener{
	
	LinkedList<DBMarker> markerList = new LinkedList<DBMarker>();
	LinkedList<GPS> gpsList = new LinkedList<GPS>();
	HashMap<DBMarker, Marker> markerHashTable = new HashMap<DBMarker, Marker>();
	boolean currentlyTracking = false;
	int gpsFreq = 5000;
	Route thisRoute = new Route();
	Database database = new Database(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Create the map Fragment
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        
	}
	
	//Creates a marker on the map based on the current location and links it with the current marker
	public void createMarker(GoogleMap map, DBMarker marker)
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
	public void deleteMarker(GoogleMap map, DBMarker marker)
	{
		Marker locationMarker = markerHashTable.get(marker);
		locationMarker.remove();
	}

	//Get location every 3 seconds and save a GPS object to the GPS list.
	//Add a line to the new location
	public void startTracking(GoogleMap map)
	{
		currentlyTracking = true;
		long time = System.currentTimeMillis();
		PolylineOptions lineOptions = new PolylineOptions();
		boolean justStarted = true;
		
		while(currentlyTracking)
		{
			if(System.currentTimeMillis() - time > gpsFreq)
			{
				if(justStarted == true)
				{
					justStarted = false;
					thisRoute.timeStart = System.currentTimeMillis();
				}
				//Call get location function
		        Location location = getLocation();
		        
	        	//Create a new GPS object
	        	GPS gpsObject = new GPS();
	        	gpsObject.latitude = location.getLatitude();
	        	gpsObject.longitude = location.getLongitude();
	        	gpsObject.time = location.getTime();
	        	gpsList.add(gpsObject);
	        	
				LatLng thisLocation = new LatLng(location.getLatitude(), location.getLongitude());
				lineOptions.add(thisLocation);
				
				map.addPolyline(lineOptions);
			}
		}
	}
	
	//Stops tracking and saves the route data to the database
	public void stopTracking(String routeName, String notes, String location)
	{
		currentlyTracking = false;
		thisRoute.timeEnd = System.currentTimeMillis();
		thisRoute.routeName = routeName;
		thisRoute.notes = notes;
		thisRoute.location = location;
		
		database.addNewRoute(gpsList, markerList, thisRoute.timeStart, thisRoute.timeEnd, thisRoute.notes, thisRoute.routeName, thisRoute.location);
	}
	
	//Draws a line on the map for a given linked list of GPS objects
	public void drawPath(GoogleMap map, LinkedList<GPS> gpsList)
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
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        
        return locationManager.getLastKnownLocation(provider);
	}

	//Fill in code here for what to do when Google Maps Marker is clicked
	//Check which DBMarker is connected with argument Marker and go from there
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

}
