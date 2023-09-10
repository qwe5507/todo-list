package com.demo.todolist.web;


import com.demo.todolist.dto.ResponseDto;
import com.demo.todolist.service.VerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/verify")
@RestController
public class VerifyController {

    private final VerifyService verifyService;

    @GetMapping("/account")
    public ResponseEntity verifyAccount(@RequestParam("accountId") String accountId) {
        verifyService.계정중복체크(accountId);

        return new ResponseEntity<>(new ResponseDto<>(1, "계정 중복 체크 성공", null), HttpStatus.OK);
    }

    @GetMapping("/nickname")
    public ResponseEntity verifyNickname(@RequestParam("nickname") String nickname) {
        verifyService.닉네임중복체크(nickname);

        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 닉네임 중복 체크 성공", null), HttpStatus.OK);
    }

}
