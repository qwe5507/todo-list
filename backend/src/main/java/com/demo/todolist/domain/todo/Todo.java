package com.demo.todolist.domain.todo;

import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserEnum;
import com.demo.todolist.dto.todo.TodoReqDto;
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
@Table(name = "todo_tb")
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, length = 20)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TodoEnum status;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = false, name = "is_delete")
    private boolean isDelete;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Todo(Long id, User user, String title, TodoEnum status, String content, boolean isDelete, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.status = status;
        this.content = content;
        this.isDelete = isDelete;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void delete() {
        this.isDelete = true;
    }

    public void modify(TodoReqDto.ModifyReqDto modifyReqDto) {
        this.title = modifyReqDto.getTitle();
        this.content = modifyReqDto.getContent();
    }

    public void changeStatus(TodoReqDto.ModifyStatusReqDto modifyStatusReqDto) {
        this.status = TodoEnum.valueOf(modifyStatusReqDto.getStatus());
    }
}
