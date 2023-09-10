package com.demo.todolist.web;

import com.demo.todolist.config.auth.LoginUser;
import com.demo.todolist.dto.ResponseDto;
import com.demo.todolist.dto.user.UserReqDto;
import com.demo.todolist.dto.user.UserReqDto.JoinReqDto;
import com.demo.todolist.dto.user.UserResDto.JoinResDto;
import com.demo.todolist.dto.user.UserResDto.LoginResDto;
import com.demo.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {
        JoinResDto joinResDto = userService.회원가입(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinResDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getUserInfo(@AuthenticationPrincipal LoginUser loginUser) {
        LoginResDto loginResDto = userService.사용자정보조회(loginUser.getUser().getAccountId());
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 정보 조회 성공", loginResDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteDelete(@AuthenticationPrincipal LoginUser loginUser) {
        userService.회원탈퇴(loginUser.getUser().getAccountId());
        return new ResponseEntity<>(new ResponseDto<>(1, "회원탈퇴 성공", ""), HttpStatus.NO_CONTENT);
    }

}
