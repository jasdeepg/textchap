/*
 * Copyright 2013 TextChap. 
 * 
 * Author: ccstevens.
 * 
 */

package com.example.textchap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Called when the user clicks the launch chaperon button. */
	public void launchChaperonActivity(View view) {
	    Intent intent;
		
		//
		// Launch the chaperon activity.
		//
	    
	    intent = new Intent(this, ChaperonActivity.class);
		startActivity(intent);
		return;
	}
	
	/** Called when the user clicks the launch conversation button. */
	public void launchUserConversationActivity(View view) {
	    Intent intent;
		
		//
		// Launch the user conversation activity.
		//
	    
	    intent = new Intent(this, UserConversationActivity.class);
		startActivity(intent);
		return;
	}
	
	/** Called when the user clicks the launch conversation button. */
	public void launchChaperonConversationActivity(View view) {
	    Intent intent;
		
		//
		// Launch the chaperon conversation activity.
		//
	    
	    intent = new Intent(this, ChaperonConversationActivity.class);
		startActivity(intent);
		return;
	}
}
