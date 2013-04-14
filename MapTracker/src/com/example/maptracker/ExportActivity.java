package com.example.maptracker;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
		BaseExpandableListAdapter exListAdaptor = new ExportExpandableListAdaptor(this,test 	);
		System.out.println("Testing if this prints to the log!");
		list.setAdapter(exListAdaptor);
	}
}
