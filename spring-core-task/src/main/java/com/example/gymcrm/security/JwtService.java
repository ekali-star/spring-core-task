package com.example.gymcrm.security;

import com.example.gymcrm.config.JwtProperties;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtService {

    private final JwtProperties props;
    private final Set<String> blacklistedJtis = ConcurrentHashMap.newKeySet();

    public JwtService(JwtProperties props) {
        this.props = props;
    }

    public String generateToken(String username) {
        try {
            JWSSigner signer = new MACSigner(props.getSecret().getBytes());

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + props.getExpirationMs()))
                    .jwtID(java.util.UUID.randomUUID().toString())
                    .build();

            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            jwt.sign(signer);
            return jwt.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT", e);
        }
    }

    public JWTClaimsSet validateAndParse(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(props.getSecret().getBytes());
            if (!jwt.verify(verifier)) throw new RuntimeException("Invalid JWT signature");

            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            if (claims.getExpirationTime().before(new Date())) throw new RuntimeException("JWT expired");
            if (blacklistedJtis.contains(claims.getJWTID())) throw new RuntimeException("JWT invalidated");

            return claims;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("JWT validation failed", e);
        }
    }

    public void blacklist(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            blacklistedJtis.add(jwt.getJWTClaimsSet().getJWTID());
        } catch (Exception e) {
            throw new RuntimeException("Failed to blacklist token", e);
        }
    }
}