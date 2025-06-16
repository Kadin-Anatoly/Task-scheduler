package com.example.taskscheduler.task_scheduler.retry;

import com.example.taskscheduler.task_scheduler.entity.CustomScheduledTask;
import com.example.taskscheduler.task_scheduler.entity.CustomScheduledTask;

public class FixedDelayStrategy implements RetryStrategy{
    @Override
    public long calculateDelay(CustomScheduledTask task, int attempt) {
        return task.getRetryDelayMs();
    }
}
