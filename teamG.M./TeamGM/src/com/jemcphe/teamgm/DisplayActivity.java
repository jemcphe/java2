package com.jemcphe.teamgm;

import java.util.ArrayList;
import java.util.HashMap;

import com.jemcphe.LeagueLib.DataService;
import com.jemcphe.LeagueLib.TeamProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DisplayActivity extends Activity {

	static Context _context;
	static ListView _listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		_context = this;

		//Set the Content View
		setContentView(R.layout.display);
		
		//Create the Listview
		_listView = (ListView) this.findViewById(R.id.displayList);
		View listHeader = this.getLayoutInflater().inflate(R.layout.list_header, null);
		_listView.addHeaderView(listHeader);

		startService();

	}

	@SuppressLint("HandlerLeak")
	public void startService(){

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

						//Parse uri and use getContentResolver
						String uriString = TeamProvider.TeamData.TEAM_NAME_URI.toString() + MainActivity.field.getText().toString();
						Uri uri = Uri.parse(uriString);
						Cursor dataCursor = getContentResolver().query(uri, TeamProvider.TeamData.PROJECTION, null, null, null);

						if(dataCursor.moveToFirst() == true){
							ArrayList<HashMap<String, String>> teamList = new ArrayList<HashMap<String, String>>();

							for (int i = 0; i<dataCursor.getCount(); i++){

								//Create HashMap for data
								HashMap<String, String> displayMap = new HashMap<String, String>();
								displayMap.put("team", dataCursor.getString(1));
								displayMap.put("conference", dataCursor.getString(2));
								displayMap.put("wins", dataCursor.getString(3));
								displayMap.put("losses", dataCursor.getString(4));

								dataCursor.moveToNext();

								teamList.add(displayMap);
							}

							//Set up the Adapter
							SimpleAdapter adapter = new SimpleAdapter(_context, teamList, R.layout.list_row, 
									new String[] {"team", "conference", "wins", "losses"}, new int[] {R.id.team,R.id.conference, R.id.wins, R.id.losses});
							//Instantiate the Adapter
							_listView.setAdapter(adapter);

						}

						//								//CREATE A STRING TO HOLD INFORMATION PULLED FROM STORED FILE
						//								String teamData = FileInfo.readStringFile(_context, "team.txt", true);
						//								//CREATE JSONARRAY FROM FILE
						//								_teamObject = new JSONObject(teamData);
						//								//CREATE JSONOBJECT FROM ARRAY INDEX
						//								_data = _teamObject.getJSONArray("standing");
						//								//CALL THE UPDATEDATA FUNCTION DEFINED EARLIER
						//								updateData(_data);
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


		//CREATE MESSENGER
		Messenger dataMessenger = new Messenger(dataHandler);

		/*
		 * CREATE INTENT & PUT MESSENGER_KEY & TEAM_KEY TO BE
		 * PASSED TO THE DATASERVICE CLASS AND INITIATE THE INTENT
		 */
		Intent dataIntent = new Intent(_context, DataService.class);
		dataIntent.putExtra(DataService.MESSENGER_KEY, dataMessenger);
		dataIntent.putExtra(DataService.TEAM_KEY, MainActivity.field.getText().toString());
		startService(dataIntent);


	}

}
