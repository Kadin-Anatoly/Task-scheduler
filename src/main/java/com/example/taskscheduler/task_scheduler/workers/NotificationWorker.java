package com.example.taskscheduler.task_scheduler.workers;

import com.example.taskscheduler.task_scheduler.service.NotificationTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationWorker implements Runnable {

    private final NotificationTaskService notificationTaskService;

    @Override
    public void run() {

        Thread thread = Thread.currentThread();

        while (!thread.isInterrupted()) {
            try{
                notificationTaskService.processTasks();
                Thread.sleep(1000);
            } catch (InterruptedException e){
                thread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
