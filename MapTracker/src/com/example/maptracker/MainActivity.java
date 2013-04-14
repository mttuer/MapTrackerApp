package com.example.maptracker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	Button export;
	Button edit;
	Button menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		export = (Button) findViewById(R.id.button_export);
		export.setOnClickListener(new OnClickListener(){
		 public void onClick(View view) {
		        setResult(RESULT_OK);
				Intent i = new Intent(view.getContext(), ExportActivity.class);
		        //i.putExtra(NotesDbAdapter.KEY_ROWID, id);
		        startActivityForResult(i, 1);
		        //finish();
		 	}
		});
		
		edit = (Button) findViewById(R.id.button_edit);
		edit.setOnClickListener(new OnClickListener(){
		 public void onClick(View view) {
		        setResult(RESULT_OK);
				Intent i = new Intent(view.getContext(), EditActivity.class);
		        //i.putExtra(NotesDbAdapter.KEY_ROWID, id);
		        startActivityForResult(i, 1);
		        //finish();
		 	}
		});
		
		menu = (Button) findViewById(R.id.button_menu);
		menu.setOnClickListener(new OnClickListener(){
		 public void onClick(View view) {
		        setResult(RESULT_OK);
				Intent i = new Intent(view.getContext(), MenuActivity.class);
		        //i.putExtra(NotesDbAdapter.KEY_ROWID, id);
		        startActivityForResult(i, 1);
		        //finish();
		 	}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}

}
