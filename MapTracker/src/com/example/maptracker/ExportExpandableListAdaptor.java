package com.example.maptracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataWrappers.DBMarker;
import dataWrappers.DBRoute;



import android.content.Context;
import android.text.InputType;
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
	
	public ExportExpandableListAdaptor(Context ctx, List<DBRoute> routes, Map<DBRoute,List<DBMarker>> markers){
		//super();
		context = ctx;
		allMarkers = markers;
		this.routes = routes;
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
		// Create a vertical view to store the interface
		LinearLayout vert = new LinearLayout(context);
		vert.setOrientation(LinearLayout.VERTICAL);
		
		// create a horizontal view to store the 
		LinearLayout hz = new LinearLayout(context);
		hz.setOrientation(LinearLayout.HORIZONTAL);

		// view representing location
		TextView tv = new TextView(context);
		Date d = new Date(r.timeStart);
		Date dEnd = new Date(r.timeEnd);
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String routeTimes;
		routeTimes = sdf.format(d) + "-" + sdf.format(dEnd);
		
		tv.setText("Route Time: " + routeTimes);
		
		hz.addView(tv);
		
		// button for editing 
		Button editButton = new Button(context);
		editButton.setText("Edit Markers");
		hz.addView(editButton);
		vert.addView(hz);
		
		// Add text view for notes about the route
		FrameLayout fl = new FrameLayout(context);
		EditText edTxt = new EditText(context);
		edTxt.setLines(4);
		edTxt.setText(r.notes);
		fl.addView(edTxt);
		fl.setId(1000);
		vert.addView(fl);
		
		
		//---Add the export button --------
		Button exportButton = new Button(context);
		RelativeLayout.LayoutParams layoutParams = 
			    (RelativeLayout.LayoutParams)exportButton.getLayoutParams();
		if(layoutParams == null){
			System.out.println("Layoutparams are null");
			layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		exportButton.setText("Export");
		exportButton.setLayoutParams(layoutParams);
		exportButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				ViewGroup vg = (ViewGroup)arg0.getParent().getParent();
				View vc;
				for(int i = 0; i < vg.getChildCount(); i ++){
					
					if((vc = vg.getChildAt(i)).getId() == 1000){
						System.out.println("WORKED!?!?");
						FrameLayout lc = (FrameLayout)vc;
						lc.removeAllViews();
						TextView tv = new TextView(arg0.getContext());
						tv.setText("WORKED?!?!?");
						
						lc.addView(ExportExpandableListAdaptor.getExportButtonView(arg0.getContext()));
						
					}
				}
				((Button)arg0).setText(vg.getChildAt(1).toString());
				
			}
			
		});
		//------------------
		
		//--- Put the button in a relative layout to center it 
		RelativeLayout rl = new RelativeLayout(context);
		rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		rl.setBackgroundColor(0xFF00FF00 );
		rl.addView(exportButton);
		vert.addView(rl);
		
		return vert;
	}

	static View getExportButtonView(Context ctx){
		LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout vl = new LinearLayout(ctx);
		vl.setOrientation(LinearLayout.VERTICAL);
		Button b1 = new Button(ctx);
		Button b2 = new Button(ctx);
		Button b3 = new Button(ctx);
		Button b4 = new Button(ctx);
		b1.setText("kml");
		b2.setText("html");
		b3.setText("txt");
		b4.setText("file");
		TextView tv1 = new TextView(ctx);
		TextView tv2 = new TextView(ctx);
		TextView tv3 = new TextView(ctx);
		TextView tv4 = new TextView(ctx);
		tv1.setText("KML");
		tv2.setText("HTML");
		tv3.setText("TXT");
		tv4.setText("FILE");
		LinearLayout hz1 = new LinearLayout(ctx);
		LinearLayout hz2 = new LinearLayout(ctx);

		hz1.setOrientation(LinearLayout.HORIZONTAL);
		hz1.addView(b1,lp);
		hz1.addView(b2,lp);
		hz1.addView(b3,lp);
		hz1.addView(b4,lp);
		hz2.setOrientation(LinearLayout.HORIZONTAL);
		hz2.addView(tv1,lp);
		hz2.addView(tv2,lp);
		hz2.addView(tv3,lp);
		hz2.addView(tv4,lp);
		
		vl.addView(hz1,lp);
		vl.addView(hz2,lp);
		
		return vl;
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
