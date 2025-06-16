package com.example.taskscheduler.task_scheduler.api;

import com.example.taskscheduler.task_scheduler.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TaskSchedulerApi {

    private final TaskService taskService;

    public TaskSchedulerApi(TaskService taskService) {
        this.taskService = taskService;
    }

    public void scheduleTask(Map<String, Object> payload) {
        try {
            taskService.scheduleTask(payload);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании задачи", e);
        }
    }

    public boolean cancelTask(Long taskId) {
        return taskService.cancelTask(taskId);
    }
}
