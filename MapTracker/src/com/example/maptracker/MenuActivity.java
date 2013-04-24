package com.example.maptracker;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends Activity{
	
	private static final String TAG="MT: ";
	
	TextView textView;
	Intent intent;
	boolean tutorial;
	boolean tutorialFirst = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu2);
		
		tutorial = getIntent().getExtras().getBoolean("tutorial");
		
		if (tutorial) {
			openTutorial();
		}

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
					intent.putExtra("tutorial", tutorial);
					startActivity(intent);
					break;

				case R.id.btnBackMap:
					/*
					intent = new Intent(MenuActivity.this,
							MainActivity2.class);
					startActivity(intent);
					*/
					finish();
					break;
					
				case R.id.btnExport:
					intent = new Intent(MenuActivity.this,
							ExportActivity.class);
					intent.putExtra("tutorial", tutorial);
					startActivity(intent);
					
					//Log create marker
					Log.w(TAG, "Export, Time: " + System.currentTimeMillis());
					
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

	private void openTutorial() {
		if (tutorialFirst) {
		AlertDialog.Builder tut=new AlertDialog.Builder(MenuActivity.this);
		tut.setMessage("Click 'Application Settings' to switch off tutorial, " +
				"change GPS Tracking Sensitivity, or name your route.\n" +
				"Click 'Export' to export recorded data.");
		
		tut.setNeutralButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		tutorialFirst = false;
		tut.show();
		}
	}

	public float getTrackingAccuracy() {
		SharedPreferences accuracy = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
		return accuracy.getFloat("trackpref", (float) 3.00);
	}
	
	public String getRouteName() {
		Time now = new Time();
		now.setToNow();
		
		SharedPreferences route = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
		return route.getString("routeName", now.toString());
	}
	
	public boolean isTutorialOn() {
		SharedPreferences tut = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
		return tut.getBoolean("checkBox", tutorial);
	}
}
