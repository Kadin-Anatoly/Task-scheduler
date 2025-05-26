package com.example.taskscheduler.task_scheduler.workers;

import com.example.taskscheduler.task_scheduler.service.BillingTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillingWorker implements Runnable {

    private final BillingTaskService billingTaskService;

    @Override
    public void run() {

        Thread thread = Thread.currentThread();

        while (!thread.isInterrupted()) {
            try{
                billingTaskService.processTasks();
                Thread.sleep(1000);
            } catch (InterruptedException e){
                thread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
