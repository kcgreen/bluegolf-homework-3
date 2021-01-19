package com.example.token.controller;

public class TokenRequest {
	
	private String userName;
	private String tournamentName;
	
	// Constructors
	public TokenRequest () {
		;
	}
	
	public TokenRequest(String userName, String tournamentName) {
		this.userName = userName;
		this.tournamentName = tournamentName;
	}
	
	
	// Getters
	public String getUserName() {
		return this.userName;
	}
	public String getTournamentName() {
		return this.tournamentName;
	}
	
	// Setters
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}
	
	@Override
	public String toString() {
		return "{\"userName\":\"" + this.userName + "\"," +
				"\"tournamentName\":\"" + this.tournamentName + "\"}";
	}

}
