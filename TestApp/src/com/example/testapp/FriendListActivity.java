package com.example.testapp;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class FriendListActivity extends ListActivity {
	
	Button statusUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);
		
		statusUpdate = (Button) findViewById(R.id.bOpenStatus);
		
		statusUpdate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FriendListActivity.this, StatusActivity.class);
				startActivity(i);
				
			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		case R.id.itemServiceStart :
			startService(new Intent(this, UpdaterService.class));
			break;
		case R.id.itemServiceStop :
			stopService(new Intent(this, UpdaterService.class));
			break;
		}

		return true;

	}
}
