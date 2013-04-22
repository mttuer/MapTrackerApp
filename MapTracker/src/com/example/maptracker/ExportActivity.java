package com.example.maptracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.Database;

import dataWrappers.DBMarker;
import dataWrappers.DBRoute;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

public class ExportActivity extends Activity {
	ExpandableListView list;
	private boolean tutorial;
	private boolean firstTutorial = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Database database = new Database(this);
		database.open();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export);
		
		tutorial = getIntent().getExtras().getBoolean("tutorial");
		
		list = (ExpandableListView)findViewById(R.id.expandableListView1);
		List<List<String>> test = new LinkedList<List<String>>();
		
		//CHANGE
		List<DBRoute> routes = new ArrayList<DBRoute>();
		DBRoute r1 = new DBRoute();
		DBRoute r2 = new DBRoute();
		r1.routeName = "Hello I'm route 1";
		r2.routeName = "Route2 Is Here";
		r1.notes = "Testing route 1";
		r2.notes = "Testing route 2 for realzz";
		r1.timeStart = System.currentTimeMillis()- 4000;
		r1.timeEnd = System.currentTimeMillis() ;
		
		r2.timeStart = System.currentTimeMillis()- 3000;
		r2.timeEnd = System.currentTimeMillis() ;
		DBMarker dbm = new DBMarker();
		
		routes.add(r1);
		routes.add(r2);
		Map<DBRoute,List<DBMarker>> allMarkers = new HashMap<DBRoute,List<DBMarker>>();
		List<DBMarker> dbms = new LinkedList<DBMarker>();
		dbms.add(dbm);
		allMarkers.put(r1, dbms);
		allMarkers.put(r2, dbms);
		routes = database.getAllRoutes();
		System.out.println(routes.size() + " routes inside the database");
		for(DBRoute dbr: routes){
			allMarkers.put(dbr, database.getMarkers(dbr.routeID));
		}
		BaseExpandableListAdapter exListAdaptor = new ExportExpandableListAdaptor(this,routes, allMarkers,database);
		System.out.println("Testing if this prints to the log!");
		list.setAdapter(exListAdaptor);
		
		if (tutorial) {
			openTutorial();
		}
	}
	private void thirdTut() {
		AlertDialog.Builder tut=new AlertDialog.Builder(ExportActivity.this);
		tut.setMessage("Choose your options for this specific export. They will control the format and amount " +
				"of data that is in your export. Click the export button to open the file in an email.\n" +
				"***.Zip file will save the data directly to the phone rather than bring up an email.***");
		
		tut.setNeutralButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		tut.show();
		
	}
	
	private void secondTut() {
		AlertDialog.Builder tut=new AlertDialog.Builder(ExportActivity.this);
		tut.setMessage("There are two views for editing markers. A list view to quickly move through markers " +
				"or a map view to spatially view where they were taken. In list view you can swipe to " +
				"delete, and in both views you click to edit.\n\n");
		
		tut.setNeutralButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				thirdTut();
			}
		});
		tut.show();
	}
	
	private void openTutorial() {
		if (firstTutorial) {
		AlertDialog.Builder tut=new AlertDialog.Builder(ExportActivity.this);
		tut.setMessage("“Here is a list of routes that you have saved. " +
				"Swipe to delete. Click to edit and export.\n\n" +
				"There is a short description of the route. Click edit markers to change your data, " +
				"or export route to choose an export type.");
		
		tut.setNeutralButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				secondTut();
			}
		});
		firstTutorial  = false;
		tut.show();
		}
	}
}
