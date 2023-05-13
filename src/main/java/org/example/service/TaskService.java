package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TaskMapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.dao.TaskDAO;
import org.example.domain.Task;
import org.example.dto.TaskDTO;
import org.example.exceptions.ParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskDAO taskDAO;

//    private TaskMapper taskMapper;
//
//    @Autowired
//    public TaskService(TaskDAO taskDAO, TaskMapper taskMapper) {
//        this.taskDAO = taskDAO;
//        this.taskMapper = taskMapper;
//    }

    @Transactional
    public Task createNewTask(TaskDTO taskDTO) {

        if (taskDTO == null) {
            log.debug("TaskDto for creating is null");
            throw new ParsingException("TaskDto for creating is null");
        }
        Task task = Task.builder()
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus())
                .build();

        // Task task1 = taskMapper.mapToEntity(taskDTO);
        taskDAO.save(task);
        log.info("new task saved, ID= : {}", task.getId());

        return task;
    }

    public Page<Task> getTasks(String page, String size) {
        log.info("getting tasks page: {}, size: {}", page, size);

        Page<Task> result = taskDAO.findAll(PageRequest.of(parser(page), parser(size)));
        return result;
    }

    public long getPagesCount(String size) {
        long taskCount = taskDAO.count();
        log.info("count all tasks: {}", taskCount);
        long result = (long) Math.ceil(taskCount / parser(size));
        return result;
    }

    public void delete(String id) {
        Task task = taskDAO.findById(Long.parseLong(id)).get();
        taskDAO.delete(task);
    }

    public Task changeTask(Task task) {

        taskDAO.save(task);
        return taskDAO.findById(task.getId()).get();
    }

    private int parser(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException exception) {
            throw new ParsingException("Argument: " + str + "is not number" + exception.getMessage());
        }
    }

}
