package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.entity.ScheduledTask;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TaskService {
    void scheduleTask(Map<String, Object> payload);
    boolean cancelTask(Long taskId);
    void processTasks();
    Page<?> getTasksByStatus(String status, int page, int size);
}
