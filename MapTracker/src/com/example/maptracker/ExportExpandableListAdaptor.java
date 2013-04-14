package com.example.maptracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataWrappers.DBMarker;
import dataWrappers.Route;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExportExpandableListAdaptor extends BaseExpandableListAdapter {
	Context context;
	List<Route> routes;
	Map<Route, List<DBMarker>> allMarkers;
	
	public ExportExpandableListAdaptor(Context ctx, List<Route> routes, Map<Route,List<DBMarker>> markers){
		//super();
		context = ctx;
		allMarkers = markers;
		this.routes = routes;
		System.out.println("Testing: From constructor");
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		Route r = allMarkers.get(arg0);
		return groupAndChild.get(arg0).get(arg1 + 1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		
		// Create a vertical view to store the interface
		LinearLayout vert = new LinearLayout(context);
		vert.setOrientation(LinearLayout.VERTICAL);
		
		// create a horizontal view to store the 
		LinearLayout hz = new LinearLayout(context);
		hz.setOrientation(LinearLayout.HORIZONTAL);

		// view representing location
		TextView tv = new TextView(context);
		tv.setText("Route Time: " + groupAndChild.get(arg0).get(arg1 + 1));
		hz.addView(tv);
		
		// button for editing 
		Button editButton = new Button(context);
		editButton.setText("Edit Markers");
		hz.addView(editButton);
	
		vert.addView(hz);
		
		// Add text view for notes about the route
		EditText edTxt = new EditText(context);
		edTxt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		edTxt.setLines(4);
		vert.addView(edTxt);
		
		
		//---Add the export button --------
		Button exportButton = new Button(context);
		RelativeLayout.LayoutParams layoutParams = 
			    (RelativeLayout.LayoutParams)exportButton.getLayoutParams();
		if(layoutParams == null){
			System.out.println("Layoutparams are null");
			layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		exportButton.setLayoutParams(layoutParams);
		//------------------
		
		//--- Put the button in a relative layout to center it 
		RelativeLayout rl = new RelativeLayout(context);
		rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		rl.addView(exportButton);
		vert.addView(rl);
		
		return vert;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return groupAndChild.get(arg0).size() - 1;
	}

	@Override
	public Object getGroup(int arg0) {
		return groupAndChild.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return groupAndChild.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		TextView tv = new TextView(context);
		tv.setText(groupAndChild.get(arg0).get(0) + " - ");
		return tv;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
