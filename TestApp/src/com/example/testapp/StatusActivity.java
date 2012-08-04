package com.example.testapp;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener,
		TextWatcher {

	private final String TAG = "StatusActivity";
	EditText et;
	Button updateButton;
	Twitter twitter;
	TextView textCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		et = (EditText) findViewById(R.id.etStatus);
		updateButton = (Button) findViewById(R.id.bUpdateStatus);
		textCount = (TextView) findViewById(R.id.tvTextCount);
		updateButton.setOnClickListener(this);

		textCount.setText(Integer.toString(140));
		textCount.setTextColor(Color.GREEN);
		et.addTextChangedListener(this);

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

	public void onClick(View v) {
		// TODO Auto-generated method stub
		String status = et.getText().toString();
		new PostToTwitter().execute(status);
		Log.d(TAG, "OnClicked");
	}

	public void afterTextChanged(Editable statusText) {
		// TODO Auto-generated method stub
		int count = 140 - statusText.length();
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN);
		if (count < 10) {
			textCount.setTextColor(Color.YELLOW);
		}
		if (count < 0) {
			textCount.setTextColor(Color.RED);
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		twitter = null;

	}

	class PostToTwitter extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... statuses) {
			// TODO Auto-generated method stub
		try {
				YambaApplication yamba = ((YambaApplication) getApplication());
				Twitter.Status status = yamba.getTwitter().updateStatus(statuses[0]);
				return status.text;
			  
			} catch (TwitterException e) {
				e.printStackTrace();
				Log.d("StatusActivity", e.toString()); 
				return "Failed to Post - ";
			} 
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG)
					.show();
		}

	}

}
