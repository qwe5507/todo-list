package com.demo.todolist.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.todolist.config.auth.LoginUser;
import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtProcess {
    private final Logger log = LoggerFactory.getLogger(getClass());


    // JWT 토큰 생성
    public static String create(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject("todo") //토큰 제목
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME)) // 토큰 만료시간 (현재 시간 + 7일 뒤)
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("accountId", loginUser.getUser().getAccountId())
                .withClaim("role", loginUser.getUser().getRole().name())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public static LoginUser verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String accountId = decodedJWT.getClaim("accountId").asString();
        String role = decodedJWT.getClaim("role").asString();
        User user = User.builder()
                .id(id)
                .accountId(accountId)
                .role(UserEnum.valueOf(role))
                .build();
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }
}
