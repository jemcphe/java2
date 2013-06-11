package com.jemcphe.LeagueLib;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
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
		
		// Define Columns
		public static final String TEAM_COLUMN = "first_name" + " " + "last_name";
		public static final String WIN_COLUMN = "won";
		public static final String LOSS_COLUMN = "lost";

		public static final String[] PROJECTION = { "_id", TEAM_COLUMN, WIN_COLUMN, LOSS_COLUMN };
		
		//Make class private
		private TeamData() {};
		
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
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
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
