package com.demo.todolist.domain.todo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TodoEnum {
    TO_DO("할일"), IN_PROGRESS("진행중"), COMPLETED("완료");

    private String value;
}
