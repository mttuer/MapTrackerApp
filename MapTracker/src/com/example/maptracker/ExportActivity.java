package com.example.maptracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dataWrappers.DBMarker;
import dataWrappers.DBRoute;


import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

public class ExportActivity extends Activity {
	ExpandableListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export);
		list = (ExpandableListView)findViewById(R.id.expandableListView1);
		List<List<String>> test = new LinkedList<List<String>>();
		List<String> first = new LinkedList<String>();
		List<String> second = new LinkedList<String>();
		first.add("First item");
		first.add("child content");
		second.add("Second item");
		second.add("child2 content");
		test.add(first);
		test.add(second);
		//CHANGE
		List<DBRoute> routes = new ArrayList<DBRoute>();
		DBRoute r1 = new DBRoute();
		DBRoute r2 = new DBRoute();
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
		
		BaseExpandableListAdapter exListAdaptor = new ExportExpandableListAdaptor(this,routes, allMarkers);
		System.out.println("Testing if this prints to the log!");
		list.setAdapter(exListAdaptor);
	}
}
