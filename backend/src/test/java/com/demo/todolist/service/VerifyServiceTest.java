package com.demo.todolist.service;

import com.demo.todolist.config.dummy.DummyObject;
import com.demo.todolist.domain.user.UserEnum;
import com.demo.todolist.domain.user.UserRepository;
import com.demo.todolist.handler.ex.CustomApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerifyServiceTest extends DummyObject {
    @InjectMocks
    private VerifyService verifyService;

    @Mock
    private UserRepository userRepository;

    @DisplayName("계정 중복 체크 로직 실행 시, 중복된 ID가 있다면 실패한다.")
    @Test
    public void 계정중복체크_실패() throws Exception {
        // given
        String accountId = "testUser";

        // stub
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(newUser("testUser", "simpson", UserEnum.USER)));

        // whenm
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            verifyService.계정중복체크(accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @DisplayName("계정 중복 체크 로직 실행 시, 중복된 ID가 없다면 성공한다.")
    @Test
    public void 계정중복체크_성공() throws Exception {
        // given
        String accountId = "testUser";

        // stub
        when(userRepository.findByAccountId(any())).thenReturn(Optional.empty());

        // when
        verifyService.계정중복체크(accountId);

        // then
    }


    @DisplayName("닉네임 중복 체크 로직 실행 시, 중복된 닉네임이 있다면 실패한다.")
    @Test
    public void 닉네임중복체크_실패() throws Exception {
        // given
        String nickname = "simpson";

        // stub
        when(userRepository.findByNickname(any())).thenReturn(Optional.of(newUser("testUser", "simpson", UserEnum.USER)));

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            verifyService.닉네임중복체크(nickname);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 닉네임입니다.");
    }

    @DisplayName("닉네임 중복 체크 로직 실행 시, 중복된 닉네임이 없다면 성공한다.")
    @Test
    public void 닉네임중복체크_성공() throws Exception {
        // given
        String nickname = "simpson";

        // stub
        when(userRepository.findByNickname(any())).thenReturn(Optional.empty());

        // when
        verifyService.닉네임중복체크(nickname);

        // then
    }
}