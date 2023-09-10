package com.demo.todolist.config.dummy;

import com.demo.todolist.domain.todo.Todo;
import com.demo.todolist.domain.todo.TodoEnum;
import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DummyObject {

    protected User newUser(String accountId, String nickname, UserEnum userEnum) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .accountId(accountId)
                .password(encPassword)
                .nickname(nickname)
                .phoneNumber("010-2729-3256")
                .isDelete(false)
                .role(userEnum)
                .build();
    };

    protected User newMockUser(Long id, String accountId, String nickname, UserEnum userEnum) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .accountId(accountId)
                .password(encPassword)
                .nickname(nickname)
                .phoneNumber("010-2729-3256")
                .isDelete(false)
                .role(userEnum)
                .build();
    };

    protected Todo newTodo(User user, String title, TodoEnum status, String content) {
        return Todo.builder()
                .user(user)
                .title(title)
                .status(status)
                .content(content)
                .isDelete(false)
                .build();
    };

    protected Todo newMockTodo(Long todoId, User user, String title, TodoEnum status, String content) {
        return Todo.builder()
                .id(todoId)
                .user(user)
                .title(title)
                .status(status)
                .content(content)
                .isDelete(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    };

}
