package com.demo.todolist.web;

import com.demo.todolist.config.dummy.DummyObject;
import com.demo.todolist.domain.todo.Todo;
import com.demo.todolist.domain.todo.TodoEnum;
import com.demo.todolist.domain.todo.TodoRepository;
import com.demo.todolist.domain.user.User;
import com.demo.todolist.domain.user.UserEnum;
import com.demo.todolist.domain.user.UserRepository;
import com.demo.todolist.dto.todo.TodoReqDto.AddReqDto;
import com.demo.todolist.dto.todo.TodoReqDto.ModifyStatusReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TodoControllerTest extends DummyObject {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSet();
        em.clear();
    }

    @DisplayName("[컨트롤러 단위테스트] 최근 Todo 조회")
    @WithUserDetails(value = "testUser1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 최근TODO조회_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/todo/latest"));
        String repsonseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + repsonseBody);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.id").value(2L));
        resultActions.andExpect(jsonPath("$.data.title").value("testTitle2"));
        resultActions.andExpect(jsonPath("$.data.content").value("testContent2"));
    }

    @DisplayName("[컨트롤러 단위테스트] TODO 추가 성공")
    @WithUserDetails(value = "testUser1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void TODO추가_test() throws Exception {
        // given
        AddReqDto addReqDto = new AddReqDto();
        addReqDto.setTitle("mockTestTitle");
        addReqDto.setContent("mockTestContent");
        String requestBody = om.writeValueAsString(addReqDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/todo")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String repsonseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + repsonseBody);

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.data.id").value(3L));
        resultActions.andExpect(jsonPath("$.data.title").value("mockTestTitle"));
    }

    @DisplayName("[컨트롤러 단위테스트] TODO 상태 수정 성공")
    @WithUserDetails(value = "testUser1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 상태수정_test() throws Exception {
        // given
        ModifyStatusReqDto modifyStatusReqDto = new ModifyStatusReqDto();
        modifyStatusReqDto.setStatus("COMPLETED");
        String requestBody = om.writeValueAsString(modifyStatusReqDto);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/todo/1/status")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String repsonseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + repsonseBody);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.title").value("testTitle1"));
        resultActions.andExpect(jsonPath("$.data.content").value("testContent1"));
        resultActions.andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }

    @DisplayName("[컨트롤러 단위테스트] TODO 상태 삭제 성공")
    @WithUserDetails(value = "testUser1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 할일삭제_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(delete("/todo/1"));
        String repsonseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + repsonseBody);

        // then
        resultActions.andExpect(status().isNoContent());
    }

    @DisplayName("[컨트롤러 단위테스트] Validation TEST")
    @WithUserDetails(value = "testUser1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void valdation_test() throws Exception {
        // given
        AddReqDto addReqDto = new AddReqDto();
        addReqDto.setTitle("TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTEST");
        String requestBody = om.writeValueAsString(addReqDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/todo")
                .content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String repsonseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + repsonseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.code").value(-1));
        resultActions.andExpect(jsonPath("$.data.title").value("2~20자 이내로 작성해주세요"));
        resultActions.andExpect(jsonPath("$.data.content").value("must not be empty"));
    }

    private void dataSet() {
        User user1 = userRepository.save(newUser("testUser1", "testNickname1", UserEnum.USER));

        Todo todo1 = newTodo(user1, "testTitle1", TodoEnum.TO_DO, "testContent1");
        Todo todo2 = newTodo(user1, "testTitle2", TodoEnum.TO_DO, "testContent2");
        todoRepository.save(todo1);
        todoRepository.save(todo2);
    }
}