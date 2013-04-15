package com.example.maptracker;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends Activity{
	TextView textView;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu2);

		Button appSettings = (Button) findViewById(R.id.btnAppSet);
		Button backMap = (Button) findViewById(R.id.btnBackMap);
		Button export = (Button) findViewById(R.id.btnExport);

		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnAppSet:
					intent = new Intent(MenuActivity.this,
							SettingsActivity.class);
					startActivity(intent);
					break;

				case R.id.btnBackMap:
					intent = new Intent(MenuActivity.this,
							MainActivity.class);
					startActivity(intent);
					break;
					
				case R.id.btnExport:
					intent = new Intent(MenuActivity.this,
							ExportActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
		};

		appSettings.setOnClickListener(listener);
		backMap.setOnClickListener(listener);
		export.setOnClickListener(listener);
		
	}

	/*
	 private void displaySharedPreferences() {
	   SharedPreferences prefs = PreferenceManager
	    .getDefaultSharedPreferences(MenuActivity.this);

	   String routeName = prefs.getString("routeName", "*******");
	   boolean tracking = prefs.getBoolean("checkBox", false);
	   String accuracyPrefs = prefs.getString("trackpref", "*******");

	   StringBuilder builder = new StringBuilder();
	   builder.append("Route Name: " + routeName + "\n");
	   builder.append("Tutorial On: " + String.valueOf(tracking) + "\n");
	   builder.append("Tracking Accuracy: " + accuracyPrefs);

	   textView.setText(builder.toString());
	}
	 */
}
