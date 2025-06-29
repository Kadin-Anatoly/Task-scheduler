package com.example.taskscheduler.task_scheduler.controller;

import com.example.taskscheduler.task_scheduler.dto.TaskResponseDto;
import com.example.taskscheduler.task_scheduler.service.TaskService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/by-status")
    public Page<TaskResponseDto> getTasksByStatus(
            @RequestParam @NotBlank String status,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return taskService.getTasksByStatus(status, page, size);
    }

}
