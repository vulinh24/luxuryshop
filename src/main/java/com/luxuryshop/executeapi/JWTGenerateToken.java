package com.luxuryshop.executeapi;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.luxuryshop.services.MyUserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTGenerateToken {
	
	//secret
	private final String JWT_SECRET = "luxuryshopbylinhvu";
	
	//expiration
	private final long JWT_EXPIRATION = 1800000L;
	
	public String generateToken(MyUserDetail userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        //String snow = now.toString();
        // Tạo chuỗi json web token từ username của user.
        return Jwts.builder()
                   .setSubject(userDetails.getUser().getUsername())
                   //.setSubject(snow)
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // use algorithm HS512
                   .compact();
    }
	
	// Lấy thông tin user từ jwt
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(JWT_SECRET)
                            .parseClaimsJws(token)
                            .getBody();

        return claims.getSubject();
    }
    
    //validate token
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
