package com.demo.todolist.config.dummy;

import com.demo.todolist.domain.todo.Todo;
import com.demo.todolist.domain.todo.TodoEnum;
import com.demo.todolist.domain.todo.TodoRepository;
import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserEnum;
import com.demo.todolist.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit extends DummyObject {

    @Profile("dev")
    @Bean
    CommandLineRunner init(UserRepository userRepository, TodoRepository todoRepository) {
        System.out.println("DummyDevInit 실행");
        return (args) -> {
            User user = userRepository.save(newUser("user", "simpson", UserEnum.USER));
            Todo todo = todoRepository.save(newTodo(user, "dummyTodoTitle", TodoEnum.TO_DO, "dummyTodoContent"));
        };
    }
}
