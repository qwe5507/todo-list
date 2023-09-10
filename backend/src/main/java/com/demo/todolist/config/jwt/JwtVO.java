package com.demo.todolist.config.jwt;

public class JwtVO {
    public static final String SECRET = "TODOMINIPROJECTSECRETKEY"; //HS256(대칭키)

    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; //만료시간, 1주일

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER = "Authorization";
}
