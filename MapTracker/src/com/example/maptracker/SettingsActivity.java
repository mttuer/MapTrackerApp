package com.example.maptracker;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   addPreferencesFromResource(R.xml.prefs);
	}


}
