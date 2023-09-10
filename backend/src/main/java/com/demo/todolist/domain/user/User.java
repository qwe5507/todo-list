package com.demo.todolist.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_tb")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20, name = "accountId")
    private String accountId;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(unique = true, nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 20, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, name = "is_delete")
    private boolean isDelete;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserEnum role; // ADMIN, USER

    @CreatedDate // Insert
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // Insert, Update
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void delete() {
        this.isDelete = true;
    }

    @Builder
    public User(Long id, String accountId, String password, String nickname, String phoneNumber, boolean isDelete, UserEnum role,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.accountId = accountId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.isDelete = isDelete;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
