package com.example.taskscheduler.task_scheduler.scripts;

import java.util.Map;

public class BillingScript implements TaskScript {
    @Override
    public void execute(Map<String, Object> parameters) throws Exception {
        System.out.println("[BILLING] Выполняю задачу..." + Thread.currentThread().getName() + " ----- " + "Параметры: " + parameters);
        Thread.currentThread().sleep(30000);
    }
}
