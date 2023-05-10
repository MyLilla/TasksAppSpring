package org.example.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Task;
import org.example.dto.TaskDTO;
import org.example.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/")
    public String hello(Model model) {

        return "redirect:tasks";
    }

    @GetMapping("/tasks")
    public String allTasks(Model model,
                           @RequestParam(value = "page", required = false, defaultValue = "0") String page,
                           @RequestParam(value = "size", required = false, defaultValue = "5") String size) {

        Page<Task> tasks = taskService.getTasks(page, size);
        int countPages = (int) taskService.getPagesCount(size);

        model.addAttribute("tasks", tasks);
        model.addAttribute("countPages", countPages);
        return "tasks";
    }

    @GetMapping("/newTask")
    public String createTask (){
        return "createTask";
    }

    @PostMapping("/createTask")
    public String createTask(TaskDTO taskDTO) {

        log.info("description for task: " + taskDTO.getDescription());
        taskService.createNewTask(taskDTO);

        return "redirect:tasks";
    }
}
