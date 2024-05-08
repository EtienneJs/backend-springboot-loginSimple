package com.etienne.backend.userapp.backenduserapp.auth;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class TokenConfig {
    public final static SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String HEADER_TOKEN = "Authorization";

    String jws = Jwts.builder().subject("Joe").signWith(SECRET_KEY).compact();


}
