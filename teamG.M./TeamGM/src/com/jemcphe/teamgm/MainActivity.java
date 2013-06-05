package com.jemcphe.teamgm;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jemcphe.LayoutLib.SpinnerDisplay;
import com.jemcphe.LayoutLib.TeamDisplay;
import com.jemcphe.LayoutLib.TeamSearch;
import com.jemcphe.LeagueLib.DataService;
import com.jemcphe.LeagueLib.FileInfo;
import com.jemcphe.LeagueLib.WebData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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
	EditText _field;
	RadioGroup teamOptions;
	String[] teamNames;
	Boolean _connected = false;
	Context _context;
	HashMap<String, String> _history;
	JSONArray _data;
	JSONObject _teamJson;
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
        
        setContentView(R.layout.applayout);
        
        _headerImage = (ImageView) findViewById(R.drawable.header);
        
        _context = this;
        
//        _data = new JSONArray();
//        _teamJson = new JSONObject();
        
        _history = new HashMap<String, String>();       
        _historyLayout = (LinearLayout) findViewById(R.id.historyLayout);
		_teamLayout = (LinearLayout) findViewById(R.id.teamDataLayout);

        //Determine data connection
        _connected = WebData.getConnectionStatus(_context);
        
        //Check for connection
        if(_connected) {
        	Toast toast = Toast.makeText(_context, "Currently connected via " + WebData.getConnectionType(_context).toString(), Toast.LENGTH_SHORT);
			toast.show();
        	Log.i("Network Connection", WebData.getConnectionType(_context));
        } else {
			Toast toast = Toast.makeText(_context, "YOU CURRENTLY HAVE NO DATA CONNECTION!!", Toast.LENGTH_LONG);
			toast.show();
        }
        
        //Create LinearLayout for Main Layout
        _mainLayout = (LinearLayout) findViewById(R.layout.applayout);
        _field = (EditText) findViewById(R.id.searchField);
        Button searchButton = (Button) findViewById(R.id.searchButton);
        
        searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(_field.getText().toString().length() == 0){
					Toast toast = Toast.makeText(_context, "Please Enter A Team Name", Toast.LENGTH_LONG);
					toast.show();
				}
				
				Handler dataHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						String response = null;
						if (msg.arg1 == RESULT_OK && msg.obj != null) {
							try {
								response = (String) msg.obj;
							}
							catch (Exception e){
								Log.e("", e.getMessage().toString());
							}
						}	
					}
				};
				
				Messenger dataMessenger = new Messenger(dataHandler);
				
				Intent dataIntent = new Intent(_context, DataService.class);
				dataIntent.putExtra(DataService.MESSENGER_KEY, dataMessenger);
				dataIntent.putExtra(DataService.TEAM_KEY, _field.getText().toString());
				startService(dataIntent);
				
				
				
//				_teamLayout.setVisibility(8);
//				_historyLabel.setVisibility(8);
//				//_mainLayout.removeView(_teamDisplay);
//				getTeam(_field.getText().toString());
//				_teamLayout.setVisibility(0);
			}
		});
        
//        Button rawDataButton = (Button) findViewById(R.id.dataButton);
//        rawDataButton.setText("Show Raw Data");
//        
////        _historyLayout.addView(rawDataButton);
//
//        rawDataButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {				
//				_teamLayout.setVisibility(8);
//				_history = new HashMap<String, String>();
//				_history = getHistory();
//				Log.i("JSON PULL TEST", _history.toString());
//	    		_historyLabel = (TextView) findViewById(R.id.historyLabel);
//				_historyLabel.setText("\r\n" + _history.toString() + "\r\n");
//				_historyLabel.setVisibility(0);
//			}
//		});       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void getTeam(String team){
    	Log.i("CLICK: ", team);
    	//String baseURL = "http://jemcphe.cloudant.com/mlb/" + team;
    	String baseURL = "https://erikberg.com/mlb/standings.json";
    	//Create URL to pass to TeamRequest
    	URL finalURL;
    	try {
			finalURL = new URL(baseURL);
			TeamRequest tr = new TeamRequest();
			tr.execute(finalURL);
		} catch (MalformedURLException e) {
			Log.e("BAD URL", "MALFORMED URL");
			finalURL = null;
		}
    }
    
    
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
    

	@SuppressWarnings("unchecked")
	private HashMap<String, String> getHistory(){
    	Object stored = FileInfo.readObjectFile(_context, "history", true);

    	HashMap<String, String> history;
    	if(stored == null){
    		Toast toast = Toast.makeText(_context, "THERE IS CURRENTLY NO DATA",  Toast.LENGTH_LONG);
			toast.show();
    		history = new HashMap<String, String>();
    	} else {
    		history = (HashMap<String, String>) stored;
    		Toast toast = Toast.makeText(_context, "DATA PULLED FROM STORAGE ",  Toast.LENGTH_LONG);
			toast.show();
    	}
    	return history;
    }
    
    
    private class TeamRequest extends AsyncTask<URL, Void, String>{
    	@Override
    	protected String doInBackground(URL... urls){
    		String response = "";
    		for(URL url: urls){
    			response = WebData.getURLStringResponse(url);
    		}
    		return response;
    	}
    	
    	@Override
    	protected void onPostExecute(String result){
    		//JSON DATA RETRIEVED, Now set TeamDisplay strings
    		Log.i("URL RESPONSE", result);
    		try {
				JSONObject json = new JSONObject(result);
				JSONObject results = json.getJSONObject("info");

				Toast toast = Toast.makeText(_context, results.getString("name") + " data displayed & stored to device",  Toast.LENGTH_SHORT);
					toast.show();
					Log.i("TEAM DATA", results.toString());
					Log.i("TEST DATA GRAB", results.getString("location").toString());
					updateData(results);
					_history.put(results.getString("name"), results.toString());
					FileInfo.storeObjectFile(_context, "history", _history, true);
					FileInfo.storeStringFile(_context, "temp", results.toString(), true);

			} catch (JSONException e) {
				Toast toast = Toast.makeText(_context, "Invalid Team Entry. Please Try Again.", Toast.LENGTH_LONG);
				toast.show();
				Log.e("JSON", "JSON OBJECT EXCEPTION");
			}
    	}
    }
    
	
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
