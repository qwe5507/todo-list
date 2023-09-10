package com.demo.todolist.web;


import com.demo.todolist.config.auth.LoginUser;
import com.demo.todolist.config.jwt.JwtProcess;
import com.demo.todolist.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    @GetMapping("/token")
    public ResponseEntity getAccessToken(@AuthenticationPrincipal LoginUser loginUser) {

        return new ResponseEntity<>(new ResponseDto<>(1, "토큰 재발급 성공", JwtProcess.create(loginUser)), HttpStatus.CREATED);
    }

}
