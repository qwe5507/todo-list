package com.demo.todolist.service;

import com.demo.todolist.config.dummy.DummyObject;
import com.demo.todolist.domain.todo.Todo;
import com.demo.todolist.domain.todo.TodoEnum;
import com.demo.todolist.domain.todo.TodoRepository;
import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserEnum;
import com.demo.todolist.domain.user.UserRepository;
import com.demo.todolist.dto.todo.TodoReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.AddReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.ModifyStatusReqDto;
import com.demo.todolist.dto.todo.TodoResDto;
import com.demo.todolist.dto.todo.TodoResDto.AddResDto;
import com.demo.todolist.dto.todo.TodoResDto.DetailResDto;
import com.demo.todolist.dto.todo.TodoResDto.ModifyStatusResDto;
import com.demo.todolist.dto.todo.TodoResDto.TodoListResDto;
import com.demo.todolist.handler.ex.CustomApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest extends DummyObject {
    @InjectMocks
    TodoService todoService;

    @Mock
    TodoRepository todoRepository;

    @Mock
    UserRepository userRepository;

    @DisplayName("최근할일조회 로직 실행 시, 존재하지 않은 회원이면 실패한다.")
    @Test
    public void 최근할일조회_회원없음_실패() throws Exception {
        // given
        String accountId = "testUser";

        // stub
        when(userRepository.findByAccountId(any())).thenReturn(Optional.empty());

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.최근할일조회(accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
    }

    @DisplayName("최근할일조회 로직 실행 시, 요청 회원의 등록된 할일이 하나도 없으면 실패 한다.")
    @Test
    public void 최근할일조회_할일없음_실패() throws Exception {
        // given
        String accountId = "testUser";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        when(todoRepository.findFirstByUserOrderByCreatedAtDesc(any())).thenReturn(Optional.empty());

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.최근할일조회(accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("할일이 존재하지 않습니다.");
    }

    @DisplayName("최근할일조회 로직 실행 시, 삭제된 할일이면 실패 한다.")
    @Test
    public void 최근할일조회_삭제된할일_실패() throws Exception {
        // given
        String accountId = "testUser";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        Todo mockTodo = newMockTodo(10L, mockUser, "testTitle", TodoEnum.TO_DO, "testContent");
        mockTodo.delete();
        when(todoRepository.findFirstByUserOrderByCreatedAtDesc(any())).thenReturn(Optional.of(mockTodo));

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.최근할일조회(accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("삭제 된 할일 입니다.");
    }

    @DisplayName("최근할일조회 로직 실행 시, 중복된 회원이 아니고 삭제된 할일이 아니면 성공한다.")
    @Test
    public void 최근할일조회_성공() throws Exception {
        // given
        String accountId = "testUser";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        Todo mockTodo = newMockTodo(10L, mockUser, "testTitle", TodoEnum.TO_DO, "testContent");
        when(todoRepository.findFirstByUserOrderByCreatedAtDesc(any())).thenReturn(Optional.of(mockTodo));

        // when
        DetailResDto detailResDto = todoService.최근할일조회(accountId);

        // then
        assertThat(detailResDto.getTitle()).isEqualTo("testTitle");
        assertThat(detailResDto.getContent()).isEqualTo("testContent");
    }

    @DisplayName("할일목록조회 로직 실행 시, 존재하지 않은 회원이면 실패한다.")
    @Test
    public void 할일목록조회_회원없음_실패() throws Exception {
        // given
        int pageNo = 0;
        int pageSize = 10;
        String accountId = "testAccount";

        // stub
        when(userRepository.findByAccountId(any())).thenReturn(Optional.empty());

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.할일목록조회(pageNo, pageSize, accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
    }

    @DisplayName("할일목록조회 로직 실행 시, 할일 목록을 내림차순으로 가져온다.")
    @Test
    public void 할일목록조회_성공() throws Exception {
        // given
        int pageNo = 0;
        int pageSize = 10;
        String accountId = "testAccount";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        Todo mockTodo1 = newMockTodo(10L, mockUser, "testTitle1", TodoEnum.IN_PROGRESS, "testContent1");
        Todo mockTodo2 = newMockTodo(11L, mockUser, "testTitle2", TodoEnum.IN_PROGRESS, "testContent2");
        PageRequest mockPageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<Todo> mockPage = new PageImpl<Todo>(Arrays.asList(mockTodo1, mockTodo2), mockPageable, 2);
        when(todoRepository.findAllByUser(any(Pageable.class), any(User.class))).thenReturn(mockPage);

        // when
        TodoListResDto todoListResDto = todoService.할일목록조회(pageNo, pageSize, accountId);

        // then
        assertThat(todoListResDto.getTotalPage()).isEqualTo(1);
        assertThat(todoListResDto.getTodoDetailResList().get(0).getTitle()).isEqualTo("testTitle1");
        assertThat(todoListResDto.getTodoDetailResList().get(0).getContent()).isEqualTo("testContent1");
        assertThat(todoListResDto.getTodoDetailResList().get(1).getTitle()).isEqualTo("testTitle2");
        assertThat(todoListResDto.getTodoDetailResList().get(1).getContent()).isEqualTo("testContent2");
    }

    @DisplayName("할일추가 로직 실행 시, 존재하지 않은 회원이면 실패한다.")
    @Test
    public void 할일추가_회원없음_실패() throws Exception {
        // given
        AddReqDto addReqDto = new AddReqDto();
        addReqDto.setTitle("testTitle");
        addReqDto.setContent("testContent");
        String accountId = "testAccount";

        // stub
        when(userRepository.findByAccountId(any())).thenReturn(Optional.empty());

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.할일추가(addReqDto, accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
    }

    @DisplayName("할일추가 로직 실행 시, 등록된 회원의 요청이면 성공한다.")
    @Test
    public void 할일추가_성공() throws Exception {
        // given
        AddReqDto addReqDto = new AddReqDto();
        addReqDto.setTitle("testTitle");
        addReqDto.setContent("testContent");
        String accountId = "testAccount";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        Todo mockTodo = newMockTodo(10L, mockUser, "testTitle", TodoEnum.IN_PROGRESS, "testContent");
        when(todoRepository.save(any())).thenReturn(mockTodo);

        // when
        AddResDto addResDto = todoService.할일추가(addReqDto, accountId);

        // then
        assertThat(addResDto.getTitle()).isEqualTo("testTitle");
        assertThat(addResDto.getUserId()).isEqualTo(10L);
    }

    @DisplayName("할일삭제 로직 실행 시, 존재하지 않은 회원이면 실패한다.")
    @Test
    public void 할일삭제_회원없음_실패() throws Exception {
        // given
        Long todoId = 10L;
        String accountId = "testAccount";

        // stub
        when(userRepository.findByAccountId(any())).thenReturn(Optional.empty());

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.할일삭제(todoId, accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
    }

    @DisplayName("할일삭제 로직 실행 시, 존재하지 않는 할일이면 실패한다.")
    @Test
    public void 할일삭제_할일없음_실패() throws Exception {
        // given
        Long todoId = 10L;
        String accountId = "testAccount";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        when(todoRepository.findById(any())).thenReturn(Optional.empty());

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.할일삭제(todoId, accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 Id의 할일이 존재하지 않습니다.");
    }

    @DisplayName("할일삭제 로직 실행 시, 삭제된 할일이면 실패한다.")
    @Test
    public void 할일삭제_삭제된할일_실패() throws Exception {
        // given
        Long todoId = 10L;
        String accountId = "testAccount";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        Todo mockTodo = newMockTodo(10L, mockUser, "testTitle", TodoEnum.IN_PROGRESS, "testContent");
        mockTodo.delete();
        when(todoRepository.findById(any())).thenReturn(Optional.of(mockTodo));

        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.할일삭제(todoId, accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("삭제 된 할일 입니다.");
    }

    @DisplayName("할일삭제 로직 실행 시, 회원과 할일이 존재하면, 삭제되지않았으면 성공한다.")
    @Test
    public void 할일삭제_성공() throws Exception {
        // given
        Long todoId = 10L;
        String accountId = "testAccount";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        Todo mockTodo = newMockTodo(10L, mockUser, "testTitle", TodoEnum.IN_PROGRESS, "testContent");
        when(todoRepository.findById(any())).thenReturn(Optional.of(mockTodo));

        // when
        todoService.할일삭제(todoId, accountId);

        // then
        assertThat(mockTodo.isDelete()).isTrue();
    }

    @DisplayName("상태변경 로직 실행 시, 회원이 없으면 실패한다.")
    @Test
    public void 상태변경_회원없음_실패() throws Exception {
        // given
        Long todoId = 10L;
        ModifyStatusReqDto modifyStatusReqDto = new ModifyStatusReqDto();
        modifyStatusReqDto.setStatus("TO_DO");
        String accountId = "testAccount";

        // stub1
        when(userRepository.findByAccountId(any())).thenReturn(Optional.empty());


        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.상태변경(todoId, modifyStatusReqDto, accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
    }

    @DisplayName("상태변경 로직 실행 시, 할일이 없으면 실패한다.")
    @Test
    public void 상태변경_할일없음_실패() throws Exception {
        // given
        Long todoId = 10L;
        ModifyStatusReqDto modifyStatusReqDto = new ModifyStatusReqDto();
        modifyStatusReqDto.setStatus("TO_DO");
        String accountId = "testAccount";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        when(todoRepository.findById(any())).thenReturn(Optional.empty());


        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.상태변경(todoId, modifyStatusReqDto, accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 Id의 할일이 존재하지 않습니다.");
    }

    @DisplayName("상태변경 로직 실행 시, 삭제된 할일이면 실패한다.")
    @Test
    public void 상태변경_삭제된할일_실패() throws Exception {
        // given
        Long todoId = 10L;
        ModifyStatusReqDto modifyStatusReqDto = new ModifyStatusReqDto();
        modifyStatusReqDto.setStatus("TO_DO");
        String accountId = "testAccount";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        Todo mockTodo = newMockTodo(10L, mockUser, "testTitle", TodoEnum.IN_PROGRESS, "testContent");
        mockTodo.delete();
        when(todoRepository.findById(any())).thenReturn(Optional.of(mockTodo));


        // when
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            todoService.상태변경(todoId, modifyStatusReqDto, accountId);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo("삭제 된 할일 입니다.");
    }

    @DisplayName("상태변경 로직 실행 시, 회원과 할일이 존재하고 삭제되지 않은 할일이면 성공한다.")
    @Test
    public void 상태변경_성공() throws Exception {
        // given
        Long todoId = 10L;
        ModifyStatusReqDto modifyStatusReqDto = new ModifyStatusReqDto();
        modifyStatusReqDto.setStatus("COMPLETED");
        String accountId = "testAccount";

        // stub1
        User mockUser = newMockUser(10L, "testUser", "testNickname", UserEnum.USER);
        when(userRepository.findByAccountId(any())).thenReturn(Optional.of(mockUser));

        // stub2
        Todo mockTodo = newMockTodo(10L, mockUser, "testTitle", TodoEnum.IN_PROGRESS, "testContent");
        when(todoRepository.findById(any())).thenReturn(Optional.of(mockTodo));


        // when
        ModifyStatusResDto modifyStatusResDto = todoService.상태변경(todoId, modifyStatusReqDto, accountId);

        // then
        assertThat(modifyStatusResDto.getStatus()).isEqualTo("COMPLETED");
    }
}