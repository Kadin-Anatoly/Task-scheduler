package com.example.taskscheduler.task_scheduler.retry;

import com.example.taskscheduler.task_scheduler.entity.CustomScheduledTask;

public interface RetryStrategy {
    long calculateDelay(CustomScheduledTask task, int attempt);
}
