package com.example.cdwebbackend.components;

import com.example.cdwebbackend.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.DialectOverride;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor
@Component
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenUtil {
    //@Value("${jwt.expiration}")
    //sua lai
    private long expiration = 36000;

    private String secretKey = "laRfGOo4z4/olswnweG9AR3XTyQTaRwnqCbc/pw9LBM=";

    public String generateToken(UserEntity user) throws Exception {
        // properties => claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        //claims.put("password", user.getPassword());

        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 10000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) // sử dụng secretKey để ký
                    .compact();

            System.out.println("Generated Token: " + token);
            System.out.println("Secret Key: " + secretKey); // In ra để kiểm tra xem secretKey có đúng không

            return token;
        } catch (Exception e) {
            throw new InvalidParameterException("Cannot create jwt token, error: " + e.getMessage());
        }
    }

    private Key getSignInKey(){
           byte[]bytes = Decoders.BASE64.decode(secretKey);
           return Keys.hmacShaKeyFor(bytes);
    }
    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[]keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        System.out.println("SecretKey: "+ secretKey);
        return secretKey;
    }

    private Claims extractAllClaims(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())  // sử dụng đúng secretKey
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // Xử lý khi chữ ký không khớp hoặc token bị sửa đổi
            throw new InvalidParameterException("Invalid JWT token or signature mismatch: " + e.getMessage());
        }
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //check expiration
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        System.out.println("Validate token:");
        System.out.println("- Username in token: " + username);
        System.out.println("- Username in userDetails: " + userDetails.getUsername());
        System.out.println("- Token expired: " + isTokenExpired(token));
        System.out.println("Token co true khong?" + (username.equals(userDetails.getUsername())&& !isTokenExpired(token)));
        return(username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

}

