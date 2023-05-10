package org.example.service;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TaskService {

    @Autowired
    private TaskDAO taskDAO;

    @Transactional
    public Task createNewTask(TaskDTO taskDTO) {

        Task task = Task.builder()
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus())
                .build();

        taskDAO.save(task);
        log.info("new task saved, ID= : " + task.getId());

        return task;
    }

    public Page<Task> getTasks(String page, String size) {
        return taskDAO.findAll(PageRequest.of(parser(page), parser(size)));
    }

    public long getPagesCount(String size) {
        long taskCount = taskDAO.count();
        return taskCount / parser(size);
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
