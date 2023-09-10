package com.demo.todolist.service;

import com.demo.todolist.domain.todo.Todo;
import com.demo.todolist.domain.todo.TodoRepository;
import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserRepository;
import com.demo.todolist.dto.todo.TodoReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.AddReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.ModifyReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.ModifyStatusReqDto;
import com.demo.todolist.dto.todo.TodoResDto;
import com.demo.todolist.dto.todo.TodoResDto.AddResDto;
import com.demo.todolist.dto.todo.TodoResDto.DetailResDto;
import com.demo.todolist.dto.todo.TodoResDto.ModifyResDto;
import com.demo.todolist.dto.todo.TodoResDto.ModifyStatusResDto;
import com.demo.todolist.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    private final String EMPTY_USER_MSG = "해당 회원이 존재하지 않습니다.";
    private final String EMPTY_TODO_MSG = "해당 Id의 할일이 존재하지 않습니다.";
    private final String DELETE_TODO_MSG = "삭제 된 할일 입니다.";

    public DetailResDto 최근할일조회(String accountId) {
        User userPS = findUserByAccountIdOrThrow(accountId);

        Todo todoPS = todoRepository.findFirstByUserOrderByCreatedAtDesc(userPS)
                .orElseThrow(() -> new CustomApiException("할일이 존재하지 않습니다."));

        if (todoPS.isDelete()) {
            throw new CustomApiException(DELETE_TODO_MSG);
        }

        return new DetailResDto(todoPS);
    }

    public TodoResDto.TodoListResDto 할일목록조회(int pageNo, int pageSize, String accountId) {
        User userPS = findUserByAccountIdOrThrow(accountId);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<Todo> page = todoRepository.findAllByUser(pageable, userPS);

        return new TodoResDto.TodoListResDto(userPS, page);

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public AddResDto 할일추가(AddReqDto addReqDto, String accountId) {
        User userPS = findUserByAccountIdOrThrow(accountId);

        Todo todo = addReqDto.toEntity(userPS);

        Todo todoPS = todoRepository.save(todo);

        return new AddResDto(todoPS);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void 할일삭제(Long todoId, String accountId) {
        checkUserByAccountIdOrThrow(accountId);

        Todo todoPS = findTodoByIdOrThrow(todoId);

        if (todoPS.isDelete()) {
            throw new CustomApiException(DELETE_TODO_MSG);
        }

        todoPS.delete();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ModifyResDto 할일수정(Long todoId, ModifyReqDto modifyReqDto, String accountId) {
        checkUserByAccountIdOrThrow(accountId);

        Todo todoPS = findTodoByIdOrThrow(todoId);

        if (todoPS.isDelete()) {
            throw new CustomApiException(DELETE_TODO_MSG);
        }

        todoPS.modify(modifyReqDto);

        return new ModifyResDto(todoPS);
    }

    public DetailResDto 할일단건조회(Long todoId, String accountId) {
        checkUserByAccountIdOrThrow(accountId);

        Todo todoPS = findTodoByIdOrThrow(todoId);

        if (todoPS.isDelete()) {
            throw new CustomApiException(DELETE_TODO_MSG);
        }

        return new DetailResDto(todoPS);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ModifyStatusResDto 상태변경(Long todoId, ModifyStatusReqDto modifyStatusReqDto, String accountId) {
        checkUserByAccountIdOrThrow(accountId);

        Todo todoPS = findTodoByIdOrThrow(todoId);

        if (todoPS.isDelete()) {
            throw new CustomApiException(DELETE_TODO_MSG);
        }

        todoPS.changeStatus(modifyStatusReqDto);

        return new ModifyStatusResDto(todoPS);
    }

    private void checkUserByAccountIdOrThrow(String accountId) {
        userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new CustomApiException(EMPTY_USER_MSG));
    }

    private User findUserByAccountIdOrThrow(String accountId) {
        return userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new CustomApiException(EMPTY_USER_MSG));
    }

    private Todo findTodoByIdOrThrow(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomApiException(EMPTY_TODO_MSG));
    }

}
