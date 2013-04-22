package com.example.maptracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Database;

import dataWrappers.DBMarker;
import dataWrappers.DBRoute;
import dataWrappers.GPS;



import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExportExpandableListAdaptor extends BaseExpandableListAdapter {
	Context context;
	List<DBRoute> routes;
	Map<DBRoute, List<DBMarker>> allMarkers;
	Database db;
	public ExportExpandableListAdaptor(Context ctx, List<DBRoute> routes, Map<DBRoute,List<DBMarker>> markers, Database db){
		//super();
		context = ctx;
		allMarkers = markers;
		this.routes = routes;
		this.db = db;
		System.out.println("Testing: From constructor");
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		DBRoute r = routes.get(arg0);
		return allMarkers.get(arg0);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		DBRoute r = routes.get(arg0);
		FrameLayout fl = new FrameLayout(context);
		fl.setBackgroundColor(Color.BLUE);
		LayoutInflater.from(context).inflate(R.layout.route_export_view, fl, true);

		// Set the route time
		TextView routeTime = (TextView)fl.findViewById(R.id.routeTime);
		Date d = new Date(r.timeStart);
		Date dEnd = new Date(r.timeEnd);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String routeTimes = sdf.format(d) + "-" + sdf.format(dEnd);
		System.out.println(routeTime == null);
		routeTime.setText(routeTimes);

		//Set the export view
		Button b = (Button)fl.findViewById(R.id.button2);

		EditText editText = (EditText)fl.findViewById(R.id.editText1);
		LinearLayout linLayout = (LinearLayout)fl.findViewById(R.id.exportOptions);
		editText.setVisibility(EditText.VISIBLE);
		linLayout.setVisibility(LinearLayout.GONE);
		
		//Set the button to switch to Export Options
		b.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				System.out.println("In OnClick Method");
				EditText editText = (EditText)((LinearLayout)arg0.getParent()).findViewById(R.id.editText1);
				LinearLayout linLayout = (LinearLayout)((LinearLayout)arg0.getParent()).findViewById(R.id.exportOptions);
				Button but = ((Button)arg0);
				if(but.getText().equals("Export")){
					but.setText("Done");
					editText.setVisibility(View.GONE);
					linLayout.setVisibility(View.VISIBLE);
				}else{
					editText.setVisibility(View.VISIBLE);
					linLayout.setVisibility(View.GONE);
					but.setText("Export");
				}
				
				
			}
			
		});
		
		Button bTxt = (Button)fl.findViewById(R.id.button5);
		bTxt.setOnClickListener(new ClickForRoute(r));
		return fl;
	}
	
	
	class ClickForRoute implements OnClickListener{
		public DBRoute route;
		public ClickForRoute(DBRoute r){
			route = r;
		}
		@Override
		public void onClick(View arg0) {
			((Button)arg0).setText("Worked");
			for(GPS g: db.getGPSData(route.routeID)){
				System.out.println(g.longitude + " , " + g.latitude);
			}
		}
		
	}

	@Override
	public int getChildrenCount(int arg0) {
		return allMarkers.get(routes.get(arg0)).size();
	}

	@Override
	public Object getGroup(int arg0) {
		return routes.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return routes.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		TextView tv = new TextView(context);
		DBRoute r = routes.get(arg0);
		Date d = new Date(r.timeStart);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		tv.setText(r.routeName + " - " + sdf.format(d));
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
