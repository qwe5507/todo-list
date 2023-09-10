package com.demo.todolist.dto.todo;

import com.demo.todolist.domain.todo.Todo;
import com.demo.todolist.domain.user.User;
import com.demo.todolist.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoResDto {

    @Getter
    @Setter
    public static class TodoListResDto {
        private Long userId;
        private int totalPage;
        private long totalElements;
        private int currentPage;
        private int currentElements;
        private List<DetailResDto> todoDetailResList = new ArrayList<>();

        public TodoListResDto(User userPS, Page<Todo> page) {
            this.userId = userPS.getId();
            this.totalPage = page.getTotalPages();
            this.totalElements = page.getTotalElements();
            this.currentPage = page.getPageable().getPageNumber();
            this.currentElements = page.getNumberOfElements();
            this.todoDetailResList = page.getContent().stream()
                    .map(DetailResDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    public static class AddResDto {
        private Long id;
        private Long userId;
        private String title;
        private String createdAt;

        public AddResDto(Todo todo) {
            this.id = todo.getId();
            this.userId = todo.getUser().getId();
            this.title = todo.getTitle();
            this.createdAt = CustomDateUtil.toStringFormat(todo.getCreatedAt());
        }
    }

    @Getter
    @Setter
    public static class ModifyResDto {
        private Long id;
        private Long userId;
        private String title;
        private String status;
        private String content;
        private boolean isDelete;
        private String updateAt;
        private String createdAt;

        public ModifyResDto(Todo todo) {
            this.id = todo.getId();
            this.userId = todo.getUser().getId();
            this.title = todo.getTitle();
            this.status = todo.getStatus().name();
            this.content = todo.getContent();
            this.isDelete = todo.isDelete();
            this.updateAt = CustomDateUtil.toStringFormat(todo.getUpdatedAt());
            this.createdAt = CustomDateUtil.toStringFormat(todo.getCreatedAt());
        }
    }

    @Getter
    @Setter
    public static class DetailResDto {
        private Long id;
        private Long userId;
        private String title;
        private String status;
        private String content;
        private boolean isDelete;
        private String updateAt;
        private String createdAt;

        public DetailResDto(Todo todo) {
            this.id = todo.getId();
            this.userId = todo.getUser().getId();
            this.title = todo.getTitle();
            this.status = todo.getStatus().name();
            this.content = todo.getContent();
            this.isDelete = todo.isDelete();
            this.updateAt = CustomDateUtil.toStringFormat(todo.getUpdatedAt());
            this.createdAt = CustomDateUtil.toStringFormat(todo.getCreatedAt());
        }
    }
    @Getter
    @Setter
    public static class ModifyStatusResDto {
        private Long id;
        private Long userId;
        private String title;
        private String status;
        private String content;
        private boolean isDelete;
        private String updateAt;
        private String createdAt;

        public ModifyStatusResDto(Todo todo) {
            this.id = todo.getId();
            this.userId = todo.getUser().getId();
            this.title = todo.getTitle();
            this.status = todo.getStatus().name();
            this.content = todo.getContent();
            this.isDelete = todo.isDelete();
            this.updateAt = CustomDateUtil.toStringFormat(todo.getUpdatedAt());
            this.createdAt = CustomDateUtil.toStringFormat(todo.getCreatedAt());
        }
    }
}

