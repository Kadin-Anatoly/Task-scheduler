package com.example.taskscheduler.task_scheduler.workers;

import com.example.taskscheduler.task_scheduler.service.ReportTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportWorker implements Runnable{

    private final ReportTaskService reportTaskService;

    @Override
    public void run() {

        Thread thread = Thread.currentThread();

        while (!thread.isInterrupted()) {
            try{
                reportTaskService.processTasks();
                Thread.sleep(1000);
            } catch (InterruptedException e){
                thread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
