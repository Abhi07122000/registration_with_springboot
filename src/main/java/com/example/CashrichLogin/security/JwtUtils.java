package com.example.CashrichLogin.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtUtils {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

//	public String generateToken(Authentication authentication) {
//		Date now = new Date();
//		Date expiryDate = new Date(now.getTime() + expiration);
//		return Jwts.builder().setSubject(authentication.getName()).setIssuedAt(new Date()).setExpiration(expiryDate)
//				.signWith(SignatureAlgorithm.HS512, secret).compact();
//	}
	public String generateToken(Authentication authentication, Long userId) {
	    Date now = new Date();
	    Date expiryDate = new Date(now.getTime() + expiration);
	    return Jwts.builder()
	            .setSubject(authentication.getName())
	            .claim("userId", userId)
	            .setIssuedAt(new Date())
	            .setExpiration(expiryDate)
	            .signWith(SignatureAlgorithm.HS512, secret)
	            .compact();
	}

	public Long extractUserId(String token) {
	    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	    return claims.get("userId", Long.class);
	}

	public String getUserIdFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}

}
