package com.example.taskscheduler.task_scheduler.retry;

import com.example.taskscheduler.task_scheduler.entity.ScheduledTask;

public class FixedDelayStrategy implements RetryStrategy{
    @Override
    public long calculateDelay(ScheduledTask task, int attempt) {
        return task.getRetryDelayMs();
    }
}
