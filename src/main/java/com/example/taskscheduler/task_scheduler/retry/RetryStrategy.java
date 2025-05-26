package com.example.taskscheduler.task_scheduler.retry;

import com.example.taskscheduler.task_scheduler.entity.ScheduledTask;

public interface RetryStrategy {
    long calculateDelay(ScheduledTask task, int attempt);
}
