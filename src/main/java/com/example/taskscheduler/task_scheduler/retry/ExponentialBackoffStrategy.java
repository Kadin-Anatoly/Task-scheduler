package com.example.taskscheduler.task_scheduler.retry;

import com.example.taskscheduler.task_scheduler.entity.CustomScheduledTask;

public class ExponentialBackoffStrategy implements RetryStrategy {
    @Override
    public long calculateDelay(CustomScheduledTask task, int attempt) {
        double delay = Math.pow(task.getRetryBase(), attempt) * 1000;
        if (task.getRetryMaxDelayMs() != null && delay > task.getRetryMaxDelayMs()) {
            return task.getRetryMaxDelayMs();
        }
        return (long) delay;
    }
}
