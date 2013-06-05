package com.jemcphe.LeagueLib;
//Enum class that holds Team values
public enum Teams {
	TEXAS("Texas Rangers", "Ian Kinsler", "2nd Base", "AL West"),
	YANKEES("New York Yankees", "Derek Jeter", "Shortstop", "AL East"),
	DETROIT("Detroit Tigers", "Justin Verlander", "Pitcher", "AL Central"),
	ATLANTA("Atlanta Braves", "Jason Heyward", "Right Field", "NL East"),
	STLOUIS("St. Louis Cardinals", "Matt Holiday", "Left Field", "NL West");
	
	private final String teamName;
	private final String keyPlayer;
	private final String position;
	private final String division;
	
	
	private Teams(String teamName, String keyPlayer, String position, String division){
		this.teamName = teamName;
		this.keyPlayer = keyPlayer;
		this.position = position;
		this.division = division;
	}
	//Functions that set given value
	public String setTeamName(){
		return teamName;
	}
	public String setKeyPlayer(){
		return keyPlayer;
	}
	public String setPosition(){
		return position;
	}
	public String setDivision(){
		return division;
	}
	
	public String getTeamName(){
		return this.teamName;
	}
}
