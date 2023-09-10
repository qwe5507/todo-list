package com.demo.todolist.service;

import com.demo.todolist.config.dummy.DummyObject;
import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserEnum;
import com.demo.todolist.domain.user.UserRepository;
import com.demo.todolist.dto.user.UserReqDto.JoinReqDto;
import com.demo.todolist.dto.user.UserResDto.JoinResDto;
import com.demo.todolist.dto.user.UserResDto.LoginResDto;
import com.demo.todolist.handler.ex.CustomApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("회원가입 로직 실행 시, 요청 id와 동일한 id가 존재하면 실패한다.")
    @Test
    public void 회원가입_중복id_실패() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setId("testUser");
        joinReqDto.setPassword("1111");
        joinReqDto.setNickname("testNickname");
        joinReqDto.setPhoneNumber("010-2729-3256");

        // stub
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(newUser("testUser", "simpson", UserEnum.USER)));

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            userService.회원가입(joinReqDto);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("동일한 id가 존재합니다.");
    }

    @DisplayName("회원가입 로직 실행 시, 중복된 id가 없으면 성공한다.")
    @Test
    public void 회원가입_성공() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setId("testUser");
        joinReqDto.setPassword("1111");
        joinReqDto.setNickname("testNickname");
        joinReqDto.setPhoneNumber("010-2729-3256");

        // stub1
        when(userRepository.findByAccountId(any())).thenReturn(Optional.empty());

        // stub2
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.save(any())).thenReturn(mockUser);

        // when
        JoinResDto joinResDto = userService.회원가입(joinReqDto);

        // then
        assertThat(joinResDto.getId()).isEqualTo("testUser");
        assertThat(joinResDto.getNickname()).isEqualTo("testNickname");
        assertThat(joinResDto.getPhoneNumber()).isEqualTo("010-2729-3256");
    }

    @DisplayName("사용자 정보 조회 로직 실행 시, 요청id로 등록된 사용자가 없으면 실패한다.")
    @Test
    public void 사용자정보조회_실패() throws Exception {
        // given
        String accountId = "diffTestUser";

        // stub
        when(userRepository.findByAccountId(any())).thenReturn(Optional.empty());

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            userService.사용자정보조회(accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
    }

    @DisplayName("사용자 정보 조회 로직 실행 시, 요청id로 등록된 사용자가 없으면 실패한다.")
    @Test
    public void 사용자정보조회_성공() throws Exception {
        // given
        String accountId = "testUser";

        // stub
        User mockUser = newMockUser(10L, "testUser", "simpson", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // when
        LoginResDto loginResDto = userService.사용자정보조회(accountId);

        // then
        assertThat(loginResDto.getAccountId()).isEqualTo("testUser");
        assertThat(loginResDto.getNickname()).isEqualTo("simpson");
    }
}