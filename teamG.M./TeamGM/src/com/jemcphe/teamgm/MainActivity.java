package com.jemcphe.teamgm;

import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jemcphe.LayoutLib.SpinnerDisplay;
import com.jemcphe.LayoutLib.TeamDisplay;
import com.jemcphe.LayoutLib.TeamSearch;
import com.jemcphe.LeagueLib.DataService;
import com.jemcphe.LeagueLib.FileInfo;
import com.jemcphe.LeagueLib.TeamProvider;
import com.jemcphe.LeagueLib.WebData;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	//Create Linear Layouts
	LinearLayout _mainLayout;
	LinearLayout _historyLayout;
	LinearLayout _teamLayout;
	//Create Displays
	TeamSearch _search;
	TeamDisplay _teamDisplay;
	SpinnerDisplay _teamList;

	//Declare Variables
	ScrollView _scrollView;
	TextView _searchLabel;
	TextView dataBox;
	TextView _teamData;
	TextView _historyLabel;
	static EditText field;
	RadioGroup teamOptions;
	String[] teamNames;
	Boolean _connected = false;
	Context _context;
	HashMap<String, String> _history;
	JSONArray _data;
	JSONObject _teamObject;
	ImageView _headerImage;
	ImageView _startingImage;

	//TeamDisplay Variables for setting values
	String _teamName;
	String _pitcher;
	String _catcher;
	String _first;
	String _second;
	String _third;
	String _short;
	String _left;
	String _center;
	String _right;
	
	//FUNCTION FOR UPDATING TEAM DATA ON THE SCREEN
	public void updateData(JSONObject data){

		try {
			((TextView) findViewById(R.id.teamNameData)).setText(data.getString("location") + " " + data.getString("name"));
			((TextView) findViewById(R.id.pitcherData)).setText(data.getString("pitcher"));
			((TextView) findViewById(R.id.catcherData)).setText(data.getString("catcher"));
			((TextView) findViewById(R.id.firstData)).setText(data.getString("first"));
			((TextView) findViewById(R.id.secondData)).setText(data.getString("second"));
			((TextView) findViewById(R.id.thirdData)).setText(data.getString("third"));
			((TextView) findViewById(R.id.shortData)).setText(data.getString("short"));
			((TextView) findViewById(R.id.leftData)).setText(data.getString("left"));
			((TextView) findViewById(R.id.centerData)).setText(data.getString("center"));
			((TextView) findViewById(R.id.rightData)).setText(data.getString("right"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("JSON ERROR", e.toString());
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//SET THE MAIN VIEW
		setContentView(R.layout.applayout);
		
		//HEADER IMAGE
		_headerImage = (ImageView) findViewById(R.drawable.header);
		
		//UNIVERSAL CONTEXT VARIABLE
		_context = this;
		
		//DEFINE LAYOUT THAT WILL HOLD TEAM DATA
		_teamLayout = (LinearLayout) findViewById(R.id.teamDataLayout);

		//Determine data connection
		_connected = WebData.getConnectionStatus(_context);

		//Check for connection
		if(_connected) {
			//DISPLAY CONNECTION TYPE TO USER
			Toast toast = Toast.makeText(_context, "Currently connected via " + WebData.getConnectionType(_context).toString(), Toast.LENGTH_SHORT);
			toast.show();
			Log.i("Network Connection", WebData.getConnectionType(_context));
		} else {
			//LET USER KNOW THEY HAVE NO DATA
			Toast toast = Toast.makeText(_context, "YOU CURRENTLY HAVE NO DATA CONNECTION!!", Toast.LENGTH_LONG);
			toast.show();
		}

		//Create LinearLayout for Main Layout
		_mainLayout = (LinearLayout) findViewById(R.layout.applayout);
		
		//DEFINE EDITTEXT FIELD
		field = (EditText) findViewById(R.id.searchField);
		field.setText(TeamProvider.TeamData.CONTENT_URI.toString());
		//DEFINE THE SEARCH BUTTON
		Button searchButton = (Button) findViewById(R.id.searchButton);

		//CREATE AN ONCLICKLISTENER FOR SEARCH BUTTON THAT WILL CALL ON SERVICE CLASS
		searchButton.setOnClickListener(new OnClickListener() {
			@SuppressLint("HandlerLeak")
			@Override
			public void onClick(View v) {
				//HANDLE DATA FROM SERVICE
				Handler dataHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						String response = null;
						//CHECK FOR PROPER SERVICE COMPLETION
						if (msg.arg1 == RESULT_OK) {
							
							try {
								//TELL DEBUGGER THAT SERVICE HAS FINISHED
								response = "Service Finished";
								Log.i("Service Status", response);
								
								//CREATE A STRING TO HOLD INFORMATION PULLED FROM STORED FILE
								String teamData = FileInfo.readStringFile(_context, "team.txt", true);
								//CREATE JSONARRAY FROM FILE
								_data = new JSONArray(teamData);
								//CREATE JSONOBJECT FROM ARRAY INDEX
								_teamObject = _data.getJSONObject(0);
								//CALL THE UPDATEDATA FUNCTION DEFINED EARLIER
								//updateData(_teamObject);
								//SET THE TEAMLAYOUT VISIBILITY
								//_teamLayout.setVisibility(0);
							}
							catch (Exception e){
								/*
								 * TELL THE USER THAT THEY NEED ENTERED AN INVALID TEAM NAME
								 * OR THEY NEED TO BE CONNECTED TO INTERNET FOR TEAM INFORMATION
								 */
								Toast toast = Toast.makeText(_context, "Please Enter A Valid Team Name Or Try Connecting To Internet For This Team's Information", Toast.LENGTH_LONG);
								toast.show();

								Log.e("", e.getMessage().toString());
							}
						}	
					}
				};
				
				//CHECK FOR USER ENTRY IN EDITTEXT FIELD
				if(field.getText().toString().length() == 0){
					//TELL USER TO ENTER A TEAM
					Toast toast = Toast.makeText(_context, "Please Enter A Team Name", Toast.LENGTH_LONG);
					toast.show();
				} else {
					//CREATE MESSENGER
					Messenger dataMessenger = new Messenger(dataHandler);
					
					/*
					 * CREATE INTENT & PUT MESSENGER_KEY & TEAM_KEY TO BE
					 * PASSED TO THE DATASERVICE CLASS AND INITIATE THE INTENT
					 */
					Intent dataIntent = new Intent(_context, DataService.class);
					dataIntent.putExtra(DataService.MESSENGER_KEY, dataMessenger);
					dataIntent.putExtra(DataService.TEAM_KEY, field.getText().toString());
					startService(dataIntent);
				}
			}
		});      
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

/********************************** LEGACY CODE ***********************************/
//	@SuppressWarnings("unused")
//	private void getTeam(String team){
//		Log.i("CLICK: ", "Get Team Initiated");
//		//String baseURL = "http://jemcphe.cloudant.com/mlb/" + team;
//		//String baseURL = "https://erikberg.com/mlb/standings.json";
//		//Create URL to pass to TeamRequest
//		URL baseURL;
//		try {
//			baseURL = new URL("http://jemcphe.cloudant.com/mlb/" + team);
//			//			finalURL = new URL(baseURL);
//			//			TeamRequest tr = new TeamRequest();
//			//			tr.execute(finalURL);
//		} catch (MalformedURLException e) {
//			Log.e("BAD URL", "MALFORMED URL");
//			baseURL = null;
//		}
//	}


	//    public void readFile(String team) throws IOException, JSONException{
	//    	String stored = FileInfo.readStringFile(_context, "temp", true);
	//    	JSONObject json = new JSONObject();
	//    	json.put(team, stored);
	//    	_data.put(json);
	//    	StringBuffer teamBuffer = new StringBuffer();
	//    	for(int i=0; i<_data.length();i++){
	//    		String text = _data.getJSONObject(i).getString("name");
	//    		teamBuffer.append(text + "\r\n");
	//    	}
	//    	Log.i("DATA ARRAY", teamBuffer.toString());
	//    }


	//	@SuppressWarnings("unchecked")
	//	private HashMap<String, String> getHistory(){
	//    	Object stored = FileInfo.readObjectFile(_context, "history", true);
	//
	//    	HashMap<String, String> history;
	//    	if(stored == null){
	//    		Toast toast = Toast.makeText(_context, "THERE IS CURRENTLY NO DATA",  Toast.LENGTH_LONG);
	//			toast.show();
	//    		history = new HashMap<String, String>();
	//    	} else {
	//    		history = (HashMap<String, String>) stored;
	//    		Toast toast = Toast.makeText(_context, "DATA PULLED FROM STORAGE ",  Toast.LENGTH_LONG);
	//			toast.show();
	//    	}
	//    	return history;
	//    }


//	private class TeamRequest extends AsyncTask<URL, Void, String>{
//		@Override
//		protected String doInBackground(URL... urls){
//			String response = "";
//			for(URL url: urls){
//				response = WebData.getURLStringResponse(url);
//			}
//			return response;
//		}
//
//		@Override
//		protected void onPostExecute(String result){
//			//JSON DATA RETRIEVED, Now set TeamDisplay strings
//			Log.i("URL RESPONSE", result);
//			try {
//				JSONArray dataArray = new JSONArray();
//				JSONObject json = new JSONObject(result);
//				JSONObject results = json.getJSONObject("info");
//
//				Toast toast = Toast.makeText(_context, results.getString("name") + " data displayed & stored to device",  Toast.LENGTH_SHORT);
//				toast.show();
//				Log.i("TEAM DATA", results.toString());
//				Log.i("TEST DATA GRAB", results.getString("location").toString());
//				updateData(results);
//				dataArray.put(results);
//				//					_history.put(results.getString("name"), results.toString());
//				//					FileInfo.storeObjectFile(_context, "history", _history, true);
//				//					FileInfo.storeStringFile(_context, "temp", results.toString(), true);
//
//			} catch (JSONException e) {
//				Toast toast = Toast.makeText(_context, "Invalid Team Entry. Please Try Again.", Toast.LENGTH_LONG);
//				toast.show();
//				Log.e("JSON", "JSON OBJECT EXCEPTION");
//			}
//		}
//	}


	//	@SuppressWarnings({"rawtypes" })
	//	public void readFromFile(HashMap history) {
	//		Set historySet = history.entrySet();
	//		Iterator i = historySet.iterator();
	//		while(i.hasNext()) {
	//			Map.Entry mapEntry = (Map.Entry) i.next();
	//			//_historyLabel = new TextView(_context);
	//			//_historyLabel.setText(mapEntry.getKey().toString() + "\r\n"+ mapEntry.getKey().toString());
	//			//System.out.println("HASH KEY: "+ mapEntry.getKey());
	//		}
	//		
	//
	//	}


}
