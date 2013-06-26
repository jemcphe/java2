package com.jemcphe.LeagueLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jemcphe.teamgm.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

	TextView teamName;
	TextView conference;
	TextView rank;
	TextView gamesPlayed;
	TextView record;
	TextView streak;
	TextView average;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.display, container, false);

		teamName = (TextView) view.findViewById(R.id.teamNameData);
		conference = (TextView) view.findViewById(R.id.conferenceData);
		rank = (TextView) view.findViewById(R.id.rankData);
		gamesPlayed = (TextView) view.findViewById(R.id.gamesPlayedData);
		record = (TextView) view.findViewById(R.id.recordData);
		streak = (TextView) view.findViewById(R.id.streakData);
		average = (TextView) view.findViewById(R.id.averageData);
		
		
		return view;
		
	}
	
	public void getTeam(String teamRequested) {

//		Bundle teamData = getIntent().getExtras();

//		if(teamData != null){
//			String teamRequested = teamData.getString("team");
			Log.i("Team Requested", teamRequested);
			String JSONString = FileInfo.readStringFile(getActivity(), "team.txt", true);
			JSONObject jsonObject = null;
			JSONArray teamsArray = null;
			JSONObject team = null;

			try {
				jsonObject = new JSONObject(JSONString);
				teamsArray = jsonObject.getJSONArray(DataService.JSON_STANDING);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			//Loop through all JSON Data
			for(int i = 0; i<teamsArray.length(); i++){
				try {
					team = teamsArray.getJSONObject(i);
					if(team.getString(DataService.JSON_FIRSTNAME).contentEquals(teamRequested)){
						String teamNameData = team.getString("first_name") + " " + team.getString("last_name");
						String conferenceData = team.getString("conference");
						String gamesPlayedData = team.getString("games_played");
						String rankData = team.getString("ordinal_rank");
						String recordData = team.getString("won") + " - " + team.getString("lost");
						String streakData = team.getString("streak");
						String averageData = team.getString("win_percentage");
						Log.i("TEAM NAME JSON", teamNameData);

						teamName.setText(teamNameData);
						conference.setText(conferenceData);
						rank.setText(rankData);
						gamesPlayed.setText(gamesPlayedData);
						record.setText(recordData);
						streak.setText(streakData);
						average.setText(averageData);

						//_teamDataLayout.setVisibility(0);

						//Add objects to the cursor
						//						result.addRow(new Object[] { i + 1, team.get(DataService.JSON_FIRSTNAME), team.get(DataService.JSON_CONFERENCE), team.get(DataService.JSON_WINS), 
						//								team.get(DataService.JSON_LOSSES)});
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//		}	
	}

}
