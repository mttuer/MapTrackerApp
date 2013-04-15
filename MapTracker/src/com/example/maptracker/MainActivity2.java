package com.example.maptracker;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;

public class MainActivity2 extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	
	Button export;
	Button edit;
	Button menu;
	Button handle, drawerMenu, drawerComment, drawerCamera, drawerMarker, drawerAudio, drawerVideo;
	SlidingDrawer sliding;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		handle = (Button) findViewById(R.id.handle);
		sliding = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		sliding.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                handle.setBackgroundResource(R.drawable.arrowclose);
            }
        });
 
        sliding.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                handle.setBackgroundResource(R.drawable.arrowopen);
            }
        });
        
        drawerMenu = (Button) findViewById(R.id.drawerButtonMenu);
		drawerMenu.setOnClickListener(new OnClickListener(){
		 public void onClick(View view) {
				Intent i = new Intent(view.getContext(), MenuActivity.class);
		        startActivity(i);
		 	}
		});
		
		drawerCamera = (Button) findViewById(R.id.drawerButtonCamera);
		drawerCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
			    // start the image capture Intent
			    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		drawerVideo = (Button) findViewById(R.id.drawerButtonVideo);
		drawerVideo.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				
			    // start the image capture Intent
			    startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Image saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }

	    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}

}
