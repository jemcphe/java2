package com.jemcphe.LeagueLib;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WebData {
	
	//Global Variables
	static Boolean _isConnected = false;
	static String _connectionType = "Unavailable";
	
	//Method to return connection type
	public static String getConnectionType(Context context) {
		netInfo(context);
		return _connectionType;
	}
	
	//Method to return connection status
	public static Boolean getConnectionStatus(Context context) {
		netInfo(context);
		return _isConnected;
	}
	
	//CONNECTIVITY CHECK FUNCTION
	private static void netInfo(Context context) {
		ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		
		//Check for network connectivity
		if (networkInfo != null) {
			if(networkInfo.isConnected()) {
				_connectionType = networkInfo.getTypeName();
				_isConnected = true;
			}
		}
	}
	
	//Grab the URL Response
	public static String getURLStringResponse(URL url){
		String response = "";
		
		try {
			//CREATE CONNECTION FROM GIVEN URL & BUFFER INPUT STREAM
			URLConnection connection = url.openConnection();
			BufferedInputStream bin = new BufferedInputStream(connection.getInputStream());
			
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			
			while ((bytesRead = bin.read(contentBytes)) != -1) {
				response = new String(contentBytes,0,bytesRead);
				responseBuffer.append(response);
			}
			return responseBuffer.toString();
			
		} catch (Exception e) {
			Log.e("URL RESPONSE ERROR", e.getMessage().toString());
		}
		
		return response;
	}
}
