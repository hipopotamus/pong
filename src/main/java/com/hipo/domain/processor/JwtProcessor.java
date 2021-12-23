package com.hipo.domain.processor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hipo.domain.UserAccount;
import com.hipo.properties.JwtProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProcessor {

    public String createAuthJwtToken(UserAccount userAccount) {
        return JWT.create()
                .withSubject(userAccount.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", userAccount.getAccount().getId())
                .withClaim("username", userAccount.getAccount().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public String decodeJwtToken(String jwtToken, String secretKey, String claim) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build()
                .verify(jwtToken)
                .getClaim(claim)
                .asString();
    }
}
