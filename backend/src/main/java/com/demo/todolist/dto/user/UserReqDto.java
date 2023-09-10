package com.demo.todolist.dto.user;

import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class UserReqDto {

    @Getter
    @Setter
    public static class LoginReqDto {
        private String accountId;
        private String password;
    }

    @Setter
    @Getter
    public static class JoinReqDto {

        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요")
        @NotEmpty
        private String id;

        @Size(min = 4, max = 20)
        @NotEmpty
        private String password;

        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요")
        @NotEmpty
        private String nickname;

        // 핸드폰 번호
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "핸드폰 번호 형식으로 작성해주세요.")
        @NotEmpty
        private String phoneNumber;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .accountId(id)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .phoneNumber(phoneNumber)
                    .role(UserEnum.USER)
                    .isDelete(false)
                    .build();
        }
    }
}
