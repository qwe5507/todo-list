package com.demo.todolist.domain.todo;

import com.demo.todolist.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findAllByUser(Pageable pageable, User user);

    Optional<Todo> findFirstByUserOrderByCreatedAtDesc(User user);
}
