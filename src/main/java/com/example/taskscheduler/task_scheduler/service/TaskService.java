package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.entity.ScheduledTask;

import java.util.Map;

public interface TaskService {
    void scheduleTask(Map<String, Object> payload);
    boolean cancelTask(Long taskId);
    void processTasks();
}
