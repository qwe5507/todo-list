package com.demo.todolist.config.auth;

import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        log.info("디버그 : loadUserByUsername()");
        User userPS = userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new InternalAuthenticationServiceException("인증 실패"));

        if (userPS.isDelete()) {
            throw new InternalAuthenticationServiceException("회원탈퇴한 회원 입니다.");
        }

        return new LoginUser(userPS);
    }
}
