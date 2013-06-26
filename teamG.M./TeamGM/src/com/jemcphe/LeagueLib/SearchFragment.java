package com.jemcphe.LeagueLib;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jemcphe.teamgm.R;

import android.app.Activity;
//import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFragment extends Fragment implements OnClickListener{

	LinearLayout _mainLayout;

	//Declare Variables
	ScrollView _scrollView;
	TextView _searchLabel;
	TextView dataBox;
	TextView _teamData;
	TextView _historyLabel;
	public static EditText field;
	RadioGroup teamOptions;
	String[] teamNames;
	Boolean _connected = false;
	Context _context;
	HashMap<String, String> _history;
	JSONArray _data;
	JSONObject _teamObject;
	ImageView _headerImage;
	ImageView _startingImage;
	ListView listview;

	//Create an Interface to communicate with Activity
	public interface onSearchButtonClicked {
		void startSearchActivity(String teamSearch);
	}
	
	//Create a private connection to MainActivity method startSearchActivity
	private onSearchButtonClicked parentActivity;
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		if(activity instanceof onSearchButtonClicked){
			parentActivity = (onSearchButtonClicked) activity;
		}
		else {
			throw new ClassCastException(activity.toString() + "must implement onSearchButtonClicked");
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		_context= getActivity();
		
		//Create the LinearLayout
		View view = inflater.inflate(R.layout.applayout, container, false);

		//HEADER IMAGE
		_headerImage = (ImageView) view.findViewById(R.drawable.header);
		
		//Create LinearLayout for Main Layout
		_mainLayout = (LinearLayout) view.findViewById(R.layout.applayout);

		//DEFINE EDITTEXT FIELD
		field = (EditText) view.findViewById(R.id.searchField);
		field.setText("Texas");
		//field.setText(TeamProvider.TeamData.CONTENT_URI.toString());
		//DEFINE THE SEARCH BUTTON
		Button searchButton = (Button) view.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(this);

		//Return the Layout View
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//		@SuppressLint("HandlerLeak")
		//		@Override
		//		public void onClick(View v) {
		//CHECK FOR USER ENTRY IN EDITTEXT FIELD
		if(field.getText().toString().length() == 0){
			//TELL USER TO ENTER A TEAM

			//AppMsg toast = AppMsg.makeText(MainActivity.this, "Please Enter A Team Name", AppMsg.STYLE_ALERT);					
			Toast toast = Toast.makeText(_context, "Please Enter A Team Name", Toast.LENGTH_LONG);
			toast.show();
		} else {
			
			String teamSearch = field.getText().toString();
			
			parentActivity.startSearchActivity(teamSearch);
			/*
			 * EXPLICIT INTENT : Per requirements for Java 2 Week 3 Assignment
			 * This intent is designed to navigate user to another activity, in this
			 * case, the DisplayActivity class.
			 */
//			Intent displayIntent = new Intent(_context, DisplayActivity.class);
//			startActivity(displayIntent);

		}
	}

}


