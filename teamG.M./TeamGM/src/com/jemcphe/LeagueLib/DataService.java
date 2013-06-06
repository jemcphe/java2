package com.jemcphe.LeagueLib;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jemcphe.teamgm.MainActivity;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

public class DataService extends IntentService{

	public static final String MESSENGER_KEY = "messenger";
	public static final String TEAM_KEY = "team";
	
	
	public DataService() {
		super("DataService");
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("onHandleIntent", "Service has started");
		
		Bundle extras = intent.getExtras();
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
		String team = extras.getString(TEAM_KEY);
		
		//Create URL
		URL dataURL;
		try {
			dataURL = new URL("http://jemcphe.cloudant.com/mlb/" + team);
		} catch (MalformedURLException e) {
			Log.e("BAD URL", "MALFORMED URL");
			dataURL = null;
		}
		
		//Create String Response
		String urlResponse;
		urlResponse = WebData.getURLStringResponse(dataURL);
		
		//Create JSONObject & JSONArray from urlResponse
		Log.i("URL RESPONSE", urlResponse);
		try {
			JSONArray dataArray = new JSONArray();
			JSONObject json = new JSONObject(urlResponse);
			JSONObject results = json.getJSONObject("info");

//			Toast toast = Toast.makeText(_context, results.getString("name") + " data displayed & stored to device",  Toast.LENGTH_SHORT);
//				toast.show();
				Log.i("TEAM DATA", results.toString());
				Log.i("TEST DATA GRAB", results.getString("location").toString());
				//updateData(results);
				dataArray.put(results);
				Log.i("DATA ARRAY", dataArray.toString());
//				_history.put(results.getString("name"), results.toString());
//				FileInfo.storeObjectFile(_context, "history", _history, true);
				FileInfo.storeStringFile(this, "temp", dataArray.toString(), true);

		} catch (JSONException e) {
//			Toast toast = Toast.makeText(_context, "Invalid Team Entry. Please Try Again.", Toast.LENGTH_LONG);
//			toast.show();
			Log.e("JSON", "JSON OBJECT EXCEPTION");
		}
		
		//Send data to handler
		Message message = Message.obtain();
		message.arg1 = Activity.RESULT_OK;
		

		
		
		

		
		
	}
	
}
