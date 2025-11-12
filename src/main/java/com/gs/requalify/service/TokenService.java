package com.gs.requalify.service;

import com.gs.requalify.model.Token;
import com.gs.requalify.model.User;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class TokenService {
    private Algorithm algorithm = Algorithm.HMAC256("secret");

    public Token createToken(User user){
        var jwt = JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getUsername())
                .sign(algorithm);

        return new Token(jwt, user.getUsername(), user.getId());


    }

    public User getUserFromToken(String jwt) {
        var jwtVerified = JWT.require(algorithm).build().verify(jwt);
        return User.builder()
                .id(Long.valueOf(jwtVerified.getSubject()))
                .username(jwtVerified.getClaim("username").toString())
                .build();

    }

}
