package com.jemcphe.LeagueLib;

import android.app.IntentService;
import android.content.Intent;

public class DataService extends IntentService{

	public static final String MESSENGER_KEY = "messenger";
	public static final String TEAM_KEY = "team";
	
	
	public DataService() {
		super("DataService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}
	
}
