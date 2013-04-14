package com.example.maptracker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import dataWrappers.DBMarker;
import dataWrappers.Route;

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
		List<Route> routes = new ArrayList<Route>();
		Route r1 = new Route();
		Route r2 = new Route();
		r1.notes = "Testing route 1";
		r2.notes = "Testing route 2 for realzz";
		r1.timeStart = System.currentTimeMillis()- 4000;
		r1.timeEnd = System.currentTimeMillis() ;
		
		r2.timeStart = System.currentTimeMillis()- 3000;
		r2.timeEnd = System.currentTimeMillis() ;
		DBMarker dbm = new DBMarker();
		
		routes.add(r1);
		routes.add(r2);
		Map<Route,List<DBMarker>> allMarkers = null;
		BaseExpandableListAdapter exListAdaptor = new ExportExpandableListAdaptor(this,routes, allMarkers);
		System.out.println("Testing if this prints to the log!");
		list.setAdapter(exListAdaptor);
	}
}
