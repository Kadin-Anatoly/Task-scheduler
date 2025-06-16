package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.entity.CustomScheduledTask;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.taskscheduler.task_scheduler.repository.TasksRepository;

@Service
public class TaskLockService {

    @Autowired
    TasksRepository tasksRepository;


    @Transactional
    public CustomScheduledTask findAndMarkPendingTask(){
        Long taskId = tasksRepository.fetchNextPendingTaskId();
        if (taskId == null) return null;
        CustomScheduledTask task = tasksRepository.findTaskById(taskId);
        task.setStatus("RUNNING");
        tasksRepository.save(task);
        return task;
    }
}