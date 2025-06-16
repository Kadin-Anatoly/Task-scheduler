package com.example.taskscheduler.task_scheduler.workers;

import com.example.taskscheduler.task_scheduler.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskWorker implements Runnable {

    private final TaskService taskService;

    @Override
    public void run() {
        Thread thread = Thread.currentThread();

        while (!thread.isInterrupted()) {
            try {
                taskService.processTasks();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                thread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
