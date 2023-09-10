package com.demo.todolist.web;

import com.demo.todolist.config.auth.LoginUser;
import com.demo.todolist.dto.ResponseDto;
import com.demo.todolist.dto.todo.TodoReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.AddReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.ModifyReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.ModifyStatusReqDto;
import com.demo.todolist.dto.todo.TodoResDto;
import com.demo.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/todo")
@RestController
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/latest")
    public ResponseEntity getTodoLatest(@AuthenticationPrincipal LoginUser loginUser) {
        TodoResDto.DetailResDto detailDto = todoService.최근할일조회(loginUser.getUser().getAccountId());

        return new ResponseEntity<>(new ResponseDto<>(1, "최근 Todo 조회 성공", detailDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getTodoList(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
            , @AuthenticationPrincipal LoginUser loginUser) {
        TodoResDto.TodoListResDto todoListResDto = todoService.할일목록조회(pageNo, pageSize, loginUser.getUser().getAccountId());

        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 목록 조회 성공", todoListResDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity insertTodo(@RequestBody @Valid AddReqDto addReqDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal LoginUser loginUser) {
        TodoResDto.AddResDto todoDto = todoService.할일추가(addReqDto, loginUser.getUser().getAccountId());

        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 추가 성공", todoDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity deleteTodo(@PathVariable("todoId") Long todoId, @AuthenticationPrincipal LoginUser loginUser) {
        todoService.할일삭제(todoId, loginUser.getUser().getAccountId());

        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 삭제 성공", null), HttpStatus.OK);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity modifyTodo(@PathVariable("todoId") Long todoId, @RequestBody @Valid ModifyReqDto modifyReqDto, BindingResult bindingResult,
                                     @AuthenticationPrincipal LoginUser loginUser) {
        TodoResDto.ModifyResDto modifyResDto = todoService.할일수정(todoId, modifyReqDto, loginUser.getUser().getAccountId());

        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 수정 성공", modifyResDto), HttpStatus.OK);
    }

    @PatchMapping("/{todoId}/status")
    public ResponseEntity modifyTodoStatus(@PathVariable("todoId") Long todoId, @RequestBody @Valid ModifyStatusReqDto modifyStatusReqDto, BindingResult bindingResult,
                                           @AuthenticationPrincipal LoginUser loginUser) {
        TodoResDto.ModifyStatusResDto modifyResDto = todoService.상태변경(todoId, modifyStatusReqDto, loginUser.getUser().getAccountId());

        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 수정 성공", modifyResDto), HttpStatus.OK);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity getTodo(@PathVariable("todoId") Long todoId, @AuthenticationPrincipal LoginUser loginUser) {
        TodoResDto.DetailResDto detailDto = todoService.할일단건조회(todoId, loginUser.getUser().getAccountId());

        return new ResponseEntity<>(new ResponseDto<>(1, "Todo 단건 조회 성공", detailDto), HttpStatus.OK);
    }
}
