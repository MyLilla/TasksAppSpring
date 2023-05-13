package org.example.service;

import org.example.dao.TaskDAO;
import org.example.domain.Status;
import org.example.domain.Task;
import org.example.dto.TaskDTO;
import org.example.exceptions.ParsingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    TaskDTO taskDto;
    Task task;
    @InjectMocks
    TaskService taskService;
    @Mock
    TaskDAO taskDAO;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDTO();
        taskDto.setDescription("Example task");
        taskDto.setStatus(Status.PAUSED);

        task = Task.builder()
                .id(1L)
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .build();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createNewTask_Test() {
        when(taskDAO.save(any(Task.class))).thenReturn(task);

        Task taskActual = taskService.createNewTask(taskDto);

        assertNotNull(task);
        assertNotNull(task.getId());
        assertEquals(task.getDescription(), taskActual.getDescription());
        assertEquals(task.getStatus(), taskActual.getStatus());
    }

    @Test
    void createNewTask_Test_nullArg() {
        assertThrows(ParsingException.class,
                () -> taskService.createNewTask(null));
    }

    @Test
    void getTasks_Test() {

        Page<Task> page = Page.empty();
        when(taskDAO.findAll(any(Pageable.class)))
                .thenReturn(page);

        assertEquals(page.getTotalElements(),
                taskService.getTasks("6", "7").getTotalElements());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void getTasks_Test_negativeArgs(Integer argument) {

        assertThrows(IllegalArgumentException.class,
                () -> taskService.getTasks("1", argument.toString()));
        assertThrows(IllegalArgumentException.class,
                () -> taskService.getTasks("-2", "1"));
    }

    @Test
    void getTasks_Test_parsingArgs() {

        assertThrows(ParsingException.class,
                () -> taskService.getTasks("null", "6"));
        assertThrows(ParsingException.class,
                () -> taskService.getTasks("6", "null"));
    }

    @Test
    void getPagesCountTest() {
        when(taskDAO.count()).thenReturn(6L);
        assertEquals(2, taskService.getPagesCount("3"));
    }

    @Test
    void getPagesCountTest_zeroArg() {
        when(taskDAO.count()).thenReturn(6L);
        assertThrows(ArithmeticException.class,
                () -> taskService.getPagesCount("0"));
    }

    @Test
    void getPagesCountTest_nullArg() {
        assertThrows(ParsingException.class,
                () -> taskService.getPagesCount(null));
    }

    @Test
    void deleteTest() {

        when(taskDAO.findById(any())).thenReturn(Optional.of(task));

        taskService.delete("1");
        Mockito.verify(taskDAO, times(1))
                .delete(any(Task.class));

    }

    @Test
    void changeTask() {
    }
}
