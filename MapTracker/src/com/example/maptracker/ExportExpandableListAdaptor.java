package com.example.maptracker;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExportExpandableListAdaptor extends BaseExpandableListAdapter {
	List<List<String>> groupAndChild;
	Context context;
	String[] p = new String[]{"one","two","three"};
	String[][] c = new String[][]{{"a"},{"b"},{"c"}};
	public ExportExpandableListAdaptor(Context ctx, List<List<String>> multiDim){
		//super();
		context = ctx;
		groupAndChild = multiDim;
		System.out.println("Testing: From constructor");
	}
	@Override
	public Object getChild(int arg0, int arg1) {
		return groupAndChild.get(arg0).get(arg1 + 1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		TextView tv = new TextView(context);
		tv.setText(groupAndChild.get(arg0).get(arg1 + 1));
		//tv.setText(groupAndChild.get(arg0).get(arg1 ));
		return tv;
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
		System.out.println("Hello World");
		tv.setText(groupAndChild.get(arg0).get(0));
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
