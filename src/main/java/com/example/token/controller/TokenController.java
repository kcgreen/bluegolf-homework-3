package com.example.token.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@RestController
@RequestMapping("/token/v1")
public class TokenController {
	
	private long EXPIRATIONTIME = 1000 * 60 * 60 * 24;
	private String SECRET = "bluegolf-homework";
	
	private boolean isValid(TokenRequest tokenRequest) {
		// Perform necessary validation of userName and tournamentName within the volunteer repository.
		if (tokenRequest != null && !tokenRequest.getUserName().toUpperCase().equals("ANAKIN")) {
			return true;
		} else {
			return false;
		}
	}
	
	@PostMapping("/set")
	public TokenResponse setTolken(@RequestBody TokenRequest tokenRequest) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		
		String JWT = null;
		
		if (isValid(tokenRequest)) {
			try {
				JWT = Jwts.builder()
						.setSubject(tokenRequest.getUserName())
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
						.claim("Volunteer", ow.writeValueAsString(tokenRequest))
						.signWith(SignatureAlgorithm.HS512, SECRET)
						.compact();
			} catch (JsonProcessingException e) {
				JWT = null;
				e.printStackTrace();
			}
		}
		
		TokenResponse tokenResponse = new TokenResponse(JWT);
		
		return tokenResponse;
	}
	
	@GetMapping("/test")
	public TestResponse testToken(@RequestParam String token) {
		ObjectMapper mapper = new ObjectMapper();
		
		TokenRequest tokenRequest = null;
		boolean allow = false;
		
		try {
			tokenRequest = mapper.readValue((String) Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody()
					.get("Volunteer"), TokenRequest.class);
			if (isValid(tokenRequest)) {
				allow = true;
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		
		TestResponse testResponse = new TestResponse(tokenRequest, allow);
		
		return testResponse;
	}

}
