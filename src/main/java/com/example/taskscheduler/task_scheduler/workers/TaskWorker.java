package com.example.taskscheduler.task_scheduler.workers;

import com.example.taskscheduler.task_scheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskWorker implements Runnable {
    private final TaskService taskService;
    private volatile String category;

    @Autowired
    public TaskWorker(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void run() {
        if (category == null) {
            throw new IllegalStateException("Category not set for worker");
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                taskService.processTasks(category);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
