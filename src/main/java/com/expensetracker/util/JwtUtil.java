package com.expensetracker.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET_KEY = "mySuperSecretKeyForExpenseTrackerJwt1234567890";

	private Key getSignKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}

	//  Generate Token
	public String generateToken(String email) {

		return Jwts.builder().subject(email) // new replacement for setSubject()
				.issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
				.signWith(getSignKey()).compact();
	}

	//  Extract Claims
	public Claims extractClaims(String token) {

		return Jwts.parser().verifyWith((javax.crypto.SecretKey) getSignKey()).build().parseSignedClaims(token)
				.getPayload();
	}

	//  Extract Email
	public String extractEmail(String token) {
		return extractClaims(token).getSubject();
	}

	//  Check Expired
	public boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}

	//  Validate Token
	public boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
}
