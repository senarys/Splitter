package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
//bu sınıf bir bean nesnesi üretecek
public class JwtUtil
{
    //token imzası oluşacak
    private final SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    private final long EXPIRATION_TIME = 1000*60*60;//token süresi 1 saat sonra yeniden login

    public String generateToken(String email) //emaile göre jwt Java web token
    {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())//tokenın üretildiği tarih
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))//token geçersizlik tarihi
                .signWith(secretKey)//giriş token imzasıyla
                .compact();//tokena sıkıştırma
    }
    public String extractEmail(String token)
    {
        return extractAllClaims(token).getSubject();//tokenı ayıklayıp içinden emaili çıkaracak
    }

    public boolean isTokenExpired(String token)
    {
        return extractAllClaims(token).getExpiration().before(new Date());//tokenın içindeki expiration tarihi ile kıyaslar
    }

    public boolean isTokenValid(String token, String email)
    {
        String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email)&&!isTokenExpired(token);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(secretKey)//token imzasını kontrol eder
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
