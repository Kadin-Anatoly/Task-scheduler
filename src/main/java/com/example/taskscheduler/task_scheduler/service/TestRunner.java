package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.api.TaskSchedulerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    private TaskSchedulerApi taskSchedulerApi;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 2; i++) {
            Map<String, Object> payload = Map.of(
                    "scriptClass", "com.example.taskscheduler.task_scheduler.scripts.BillingScript",
                    "parameters", Map.of("userId", 123 + i, "amount", 500 + i * 100),
                    "scheduledTime", "2025-06-16T09:39:30",
                    "maxRetries", 3,
                    "retryStrategy", "FIXED_DELAY",
                    "retryDelayMs", 5000,
                    "retryMaxDelayMs", 60000,
                    "category", "billing"
            );

            Map<String, Object> payload2 = Map.of(
                    "scriptClass", "com.example.taskscheduler.task_scheduler.scripts.ReportScript",
                    "parameters", Map.of("userId", 123 + i, "amount", 500 + i * 100),
                    "scheduledTime", "2025-06-16T09:39:30",
                    "maxRetries", 3,
                    "retryStrategy", "FIXED_DELAY",
                    "retryDelayMs", 5000,
                    "retryMaxDelayMs", 60000,
                    "category", "reporting"
            );

            Map<String, Object> payload3 = Map.of(
                    "scriptClass", "com.example.taskscheduler.task_scheduler.scripts.EmailScript",
                    "parameters", Map.of("userId", 123 + i, "amount", 500 + i * 100),
                    "scheduledTime", "2025-06-16T09:39:30",
                    "maxRetries", 3,
                    "retryStrategy", "FIXED_DELAY",
                    "retryDelayMs", 5000,
                    "retryMaxDelayMs", 60000,
                    "category", "email"
            );

            taskSchedulerApi.scheduleTask(payload);
            taskSchedulerApi.scheduleTask(payload2);
            taskSchedulerApi.scheduleTask(payload3);

        }
    }
}
