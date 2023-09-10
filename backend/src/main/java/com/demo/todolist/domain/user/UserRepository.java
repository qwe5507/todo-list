package com.demo.todolist.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccountId(String id);

    Optional<User> findByNickname(String nickname);
}
