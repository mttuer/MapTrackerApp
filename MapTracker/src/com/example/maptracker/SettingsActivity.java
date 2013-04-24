package com.example.maptracker;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	private static final String TAG="MT: ";

	boolean tutorial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);

		//Log create marker
		Time now = new Time();
		now.setToNow();
		Log.w(TAG, "Settings, Time: " + now);

		tutorial = getIntent().getExtras().getBoolean("tutorial");

		if (tutorial) {
			openTutorial();
		}
	}
	private void openTutorial() {
		AlertDialog.Builder tut=new AlertDialog.Builder(SettingsActivity.this);
		tut.setMessage("To adjust tracking sensitivity click 'Tracking Accuracy' " +
				"then choose a number, 1 through 10, to indicate High to Low Sensitivity " +
				"according to your battery status.\n" +
				"Click 'Toggle Tutorial' to switch off tutorial. You can switch it on at any point");

		tut.setNeutralButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});

		tut.show();

	}


}
