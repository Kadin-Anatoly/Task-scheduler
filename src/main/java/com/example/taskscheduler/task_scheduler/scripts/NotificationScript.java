package com.example.taskscheduler.task_scheduler.scripts;

import java.util.Map;

public class NotificationScript implements TaskScript {
    @Override
    public void execute(Map<String, Object> parameters) throws Exception {
        System.out.println("[NOTIFICATION] Выполняю задачу..." + Thread.currentThread().getName() + " ----- " + "Параметры: " + parameters);
        Thread.currentThread().sleep(30000);
    }
}
