package com.demo.todolist.dto.todo;

import com.demo.todolist.domain.todo.Todo;
import com.demo.todolist.domain.todo.TodoEnum;
import com.demo.todolist.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class TodoReqDto {

    @Setter
    @Getter
    public static class AddReqDto {

        @Pattern(regexp = "(?i)^.{2,20}$", message = "2~20자 이내로 작성해주세요")
        @NotEmpty
        private String title;

        @Pattern(regexp = "(?i)^.{0,255}$", message = "255자 이내로 작성해주세요")
        @NotEmpty
        private String content;

        public Todo toEntity(User user) {
            return Todo.builder()
                    .user(user)
                    .title(title)
                    .status(TodoEnum.TO_DO)
                    .content(content)
                    .isDelete(false)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class ModifyReqDto {

        @Pattern(regexp = "(?i)^.{2,20}$", message = "2~20자 이내로 작성해주세요")
        @NotEmpty
        private String title;

        @Pattern(regexp = "(?i)^.{0,255}$", message = "255자 이내로 작성해주세요")
        @NotEmpty
        private String content;
    }

    @Setter
    @Getter
    public static class ModifyStatusReqDto {

        @NotBlank
        @Pattern(regexp = "^(TO_DO|IN_PROGRESS|COMPLETED)$")
        private String status;
    }
}
