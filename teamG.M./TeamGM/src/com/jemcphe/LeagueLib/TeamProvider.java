package com.jemcphe.LeagueLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class TeamProvider extends ContentProvider {

	//Create Authority
	public static final String AUTHORITY = "com.jemcphe.teamgm.teamprovider";

	public static class TeamData implements BaseColumns {
		//Create URI Definitions
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/teams");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jemcphe.teamgm.team";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item /vnd.jemcphe.teamgm.team";

		// Define Columns for erikberg API
		public static final String TEAM_COLUMN = "first_name";
		public static final String WIN_COLUMN = "won";
		public static final String LOSS_COLUMN = "lost";

		// Define Columns for ESPN API
//		public static final String TEAM_LOCATION = "location";
//		public static final String TEAM_NAME = "name";
//		public static final String TEAM_ABBREVIATION = "abbreviation";

		public static final String[] PROJECTION = { "_id", TEAM_COLUMN, WIN_COLUMN, LOSS_COLUMN };

		//Make class private
		private TeamData() {};

	}

	//Define Teams collected from JSON
	public static final int TEAMS = 1;
	public static final int TEAMS_ID = 2;

	//Create URI Matcher
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	//Setup the URI's
	static {
		uriMatcher.addURI(AUTHORITY, "teams/", TEAMS);
		uriMatcher.addURI(AUTHORITY, "teams/#", TEAMS_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub

		//Test for which uri is entered
		switch (uriMatcher.match(uri)) {
		case TEAMS:
			return TeamData.CONTENT_TYPE;

		case TEAMS_ID:
			return TeamData.CONTENT_ITEM_TYPE;

		default:
			break;
		}



		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub

		//Create Cursor
		MatrixCursor result = new MatrixCursor(TeamData.PROJECTION);

		String JSONString = FileInfo.readStringFile(getContext(), "teams.txt", true);
		JSONObject jsonObject = null;
		JSONArray teamsArray = null;
		JSONObject team = null;

		try {
			jsonObject = new JSONObject(JSONString);
			teamsArray = jsonObject.getJSONArray(DataService.JSON_STANDING);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if(teamsArray == null){
			return result;
		}

		//Test for which uri is entered
		switch (uriMatcher.match(uri)) {
		case TEAMS:

			//Loop through all JSON Data
			for(int i = 0; i<teamsArray.length(); i++){
				try {
					team = teamsArray.getJSONObject(i);

					//Add objects to the cursor
					result.addRow(new Object[] { i + 1, team.get(DataService.JSON_TEAM), team.get(DataService.JSON_WINS), 
							team.get(DataService.JSON_LOSSES)});

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			break;
		case TEAMS_ID:

			break;
		default:
			break;
		}

		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
