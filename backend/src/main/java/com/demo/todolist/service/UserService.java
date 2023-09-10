package com.demo.todolist.service;

import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserRepository;
import com.demo.todolist.dto.user.UserReqDto.JoinReqDto;
import com.demo.todolist.dto.user.UserResDto.JoinResDto;
import com.demo.todolist.dto.user.UserResDto.LoginResDto;
import com.demo.todolist.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String EMPTY_USER_MSG = "해당 회원이 존재하지 않습니다.";

    @Transactional(propagation = Propagation.REQUIRED)
    public JoinResDto 회원가입(JoinReqDto joinReqDto) {
        Optional<User> userOp = userRepository.findByAccountId(joinReqDto.getId());

        if (userOp.isPresent()) {
            throw new CustomApiException("동일한 id가 존재합니다.");
        }

        User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder));

        return new JoinResDto(userPS);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public LoginResDto 사용자정보조회(String accountId) {
        User userPS = findUserByAccountIdOrThrow(accountId);

        return new LoginResDto(userPS);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void 회원탈퇴(String accountId) {
        User userPS = findUserByAccountIdOrThrow(accountId);

        userPS.delete();
    }


    private User findUserByAccountIdOrThrow(String accountId) {
        return userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new CustomApiException(EMPTY_USER_MSG));
    }

}
