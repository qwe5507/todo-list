package com.demo.todolist.service;

import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserRepository;
import com.demo.todolist.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VerifyService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public void 계정중복체크(String accountId) {
        Optional<User> userOp = userRepository.findByAccountId(accountId);
        if (userOp.isPresent()) {
            throw new CustomApiException("이미 존재하는 회원입니다.");
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public void 닉네임중복체크(String nickname) {
        Optional<User> userOp = userRepository.findByNickname(nickname);

        if (userOp.isPresent()) {
            throw new CustomApiException("이미 존재하는 닉네임입니다.");
        }
    }
}
