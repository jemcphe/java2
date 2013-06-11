package com.jemcphe.LeagueLib;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class DataService extends IntentService{

	//CREATE CONSTANT STRINGS TO HOLD PASSED DATA
	public static final String MESSENGER_KEY = "messenger";
	public static final String TEAM_KEY = "team";
	
	//Static JSON String for pulled data
	public static final String JSON_STANDING = "standing";
	public static final String JSON_TEAM = "first_name";
	public static final String JSON_WINS = "won";
	public static final String JSON_LOSSES = "lost";
	


	public DataService() {
		super("DataService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		//LOG THAT SERVICE HAS STARTED
		Log.i("SERVICE STATUS", "Service has started");

		/*
		 * GET THE EXTRAS PASSED FROM MAINACTIVITY
		 * AND USE THE DATA TO CREATE URL REQUEST
		 */
		Bundle extras = intent.getExtras();
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
		//String team = extras.getString(TEAM_KEY);

		//Create URL
		URL dataURL;
		try {
			//THIS URL IS CREATED BASED ON USER ENTRY FROM MAINACTIVITY
			//dataURL = new URL("https://jemcphe.cloudant.com/mlb/" + team);
			dataURL = new URL("https://erikberg.com/mlb/standings.json");
		} catch (MalformedURLException e) {
			Log.e("BAD URL", "MALFORMED URL");
			dataURL = null;
		}

		/*
		 * CREATE URLRESPONSE STRING.  THIS STRING WILL BE THE PRODUCT
		 * OF WEBDATA CLASS FUNCTION getURLStringResponse(). THIS WILL
		 * MAKE THE PROPER URL CONNECTION FOR PENDING JSON DATA
		 */
		String urlResponse;
		urlResponse = WebData.getURLStringResponse(dataURL);

		//Create JSONObject & JSONArray from urlResponse
		Log.i("URL RESPONSE", urlResponse);
		try {
			//CREATE JSONARRAY & OBJECT FROM GATHERED DATA FROM URL
			JSONArray dataArray = new JSONArray();
			JSONObject json = new JSONObject(urlResponse);
			//JSONObject results = json.getJSONObject("info");
			JSONObject results = json.getJSONObject(JSON_STANDING);

			//CREATE LOGS TO VERIFY CORRECT DATA IS PULLED
			Log.i("TEAM DATA", results.toString());
			//Log.i("TEST DATA GRAB", results.toString());

			//PLACE RESULTS INTO dataArray & LOG TO CONSOLE
			dataArray.put(results);
			Log.i("DATA ARRAY", dataArray.toString());

			//STORE DATA AS A .TXT FILE TO DEVICE
			FileInfo.storeStringFile(this, "standings.txt", dataArray.toString(), true);

		} catch (JSONException e) {
			Log.e("JSON", "JSON OBJECT EXCEPTION");
		}

		//Send data to handler
		Message message = Message.obtain();
		message.arg1 = Activity.RESULT_OK;

		try {
			//SEND MESSAGE BACK TO HANDLER IN MAINACTIVITY
			messenger.send(message);
		} catch (RemoteException e) {
			Log.e("onHandleIntent", e.getMessage().toString());
			e.printStackTrace();
		}
	}

}
