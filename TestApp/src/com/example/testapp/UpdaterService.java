package com.example.testapp;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	static final String TAG = "UpdaterService";
	static final int DELAY = 60000;
	private boolean runFlag = false;
	private Updater updater;
	private YambaApplication yamba;

	  DbHelper dbHelper; // <1>
	  SQLiteDatabase db;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.yamba = (YambaApplication) getApplication();
		this.updater = new Updater();
		
		 dbHelper = new DbHelper(this); // <2>
		
		Log.d(TAG, "OnCreate");

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		this.yamba.setServiceRunning(false);
		Log.d(TAG, "OnDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "OnStarted");
		this.runFlag = true;
		this.updater.start();
		this.yamba.setServiceRunning(true);
		return START_STICKY;
	}

	private class Updater extends Thread {
		
		 List<Twitter.Status> timeline;

		public Updater() {
			super("UpdaterService-Updater");
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			UpdaterService updaterService = UpdaterService.this;
			
			while (updaterService.runFlag) {
				Log.d(TAG, "Updater running");

				try {
					
					try {
						timeline = yamba.getTwitter().getFriendsTimeline();
					} catch (TwitterException e) {
						Log.e(TAG, "Failed to connect to twitter service -", e);
					}
					
					db = dbHelper.getWritableDatabase(); // <4>

					
				     // Loop over the timeline and print it out
			          ContentValues values = new ContentValues(); // <5>
			          for (Twitter.Status status : timeline) { // <6>
			            // Insert into database
			            values.clear(); // <7>
			            values.put(DbHelper.C_ID, status.id);
			            values.put(DbHelper.C_CREATED_AT, status.createdAt.getTime());
			            values.put(DbHelper.C_SOURCE, status.source);
			            values.put(DbHelper.C_TEXT, status.text);
			            values.put(DbHelper.C_USER, status.user.name);
			            db.insertWithOnConflict(DbHelper.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE); // <8>

			            Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
			          }

			          // Close the database
			          db.close(); // <9>
			          			          
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY); // <12>
				} catch (InterruptedException e) { // <13>
					updaterService.runFlag = false;
				}
			}
		
	} 

	}

}
