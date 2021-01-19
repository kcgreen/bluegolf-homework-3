package com.example.token.controller;

public class TestResponse {
	
	private TokenRequest tokenRequest;
	private boolean allow;
	
	// Constructors
	public TestResponse () {
		;
	}
	
	public TestResponse(TokenRequest tokenRequest, boolean allow) {
		this.tokenRequest = tokenRequest;
		this.allow = allow;
	}
	
	
	// Getters
	public TokenRequest getTokenRequest() {
		return this.tokenRequest;
	}
	public boolean getAllow() {
		return this.allow;
	}
	
	// Setters
	public void setTokenRequest(TokenRequest tokenRequest) {
		this.tokenRequest = tokenRequest;
	}
	public void setAllow(boolean allow) {
		this.allow = allow;
	}
	
	@Override
	public String toString() {
		return "{\"tokenRequest\":\"" + this.tokenRequest.toString() + "\"," +
				"\"allow\":\"" + this.allow + "\"}";
	}

}
