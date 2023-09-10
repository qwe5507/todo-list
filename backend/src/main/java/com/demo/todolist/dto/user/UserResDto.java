package com.demo.todolist.dto.user;

import com.demo.todolist.domain.user.User;
import com.demo.todolist.util.CustomDateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class UserResDto {

    @Getter
    @Setter
    public static class LoginResDto {
        private String accountId;
        private String nickname;
        private String phoneNumber;
        private boolean isDelete;
        private String createdAt;

        public LoginResDto(User user) {
            this.accountId = user.getAccountId();
            this.nickname = user.getNickname();
            this.phoneNumber = user.getPhoneNumber();
            this.isDelete = user.isDelete();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }

    @ToString
    @Setter
    @Getter
    @NoArgsConstructor
    public static class JoinResDto {
        private String id;
        private String nickname;
        private String phoneNumber;

        public JoinResDto(User user) {
            this.id = user.getAccountId();
            this.nickname = user.getNickname();
            this.phoneNumber = user.getPhoneNumber();
        }
    }
}
