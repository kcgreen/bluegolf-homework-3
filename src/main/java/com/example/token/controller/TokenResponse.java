package com.example.token.controller;

public class TokenResponse {
	
	private String token;
	
	// Constructors
	public TokenResponse () {
		;
	}
	
	public TokenResponse(String token) {
		this.token = token;
	}
	
	
	// Getters
	public String getToken() {
		return this.token;
	}
	
	// Setters
	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return "{\"token\":\"" + this.token + "\"}";
	}

}
