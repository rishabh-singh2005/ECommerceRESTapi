package com.example.ecom.security;





import java.util.Date;

import javax.crypto.SecretKey;


import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private final  SecretKey SECRET_KEY = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey".getBytes());
	
	public String generateToken(String email) {
		
		return Jwts.builder()
				.subject(email)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(SECRET_KEY)
				.compact();
				
	}
	
	public String generateUserName(String token) {
		
		Claims claims = getAllClaims(token);
		
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		
		try {
		getAllClaims(token);
		return true;
		}
		catch (Exception e) {
			return false;
		}
		
	}
	
	private Claims getAllClaims(String token) {
		
		return Jwts.parser()
				.verifyWith(SECRET_KEY)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		
	}

}
