package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.entity.BillingTask;
import com.example.taskscheduler.task_scheduler.repository.BillingTaskRepository;
import com.example.taskscheduler.task_scheduler.retry.ExponentialBackoffStrategy;
import com.example.taskscheduler.task_scheduler.retry.FixedDelayStrategy;
import com.example.taskscheduler.task_scheduler.retry.RetryStrategy;
import com.example.taskscheduler.task_scheduler.scripts.TaskScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BillingTaskService implements TaskService {

    @Autowired
    private TaskLockService taskLockService;

    private final BillingTaskRepository repository;

    public BillingTaskService(BillingTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void scheduleTask(Map<String, Object> payload) {

        BillingTask task = new BillingTask();

        task.setScriptClass((String) payload.get("scriptClass"));
        task.setParameters((String) payload.get("parameters")); // JSON строка
        task.setScheduledTime(LocalDateTime.parse((String) payload.get("scheduledTime")));
        task.setMaxRetries((Integer) payload.get("maxRetries"));
        task.setRetryStrategy((String) payload.get("retryStrategy"));
        task.setRetryDelayMs((Integer) payload.get("retryDelayMs"));
        task.setRetryMaxDelayMs((Integer) payload.get("retryMaxDelayMs"));

        task.setStatus("PENDING");
        task.setAttempt(0);
        repository.save(task);
    }

    @Override
    public boolean cancelTask(Long taskId) {
        return repository.findById(taskId).map(task -> {

            if ("COMPLETED".equals(task.getStatus()) || "CANCELLED".equals(task.getStatus()) || "FAILED".equals(task.getStatus()) || "RUNNING".equals(task.getStatus())) {
                return false;
            }

            task.setStatus("CANCELLED");
            repository.save(task);
            return true;
        }).orElse(false);
    }

    public void processTasks() {
        BillingTask task = taskLockService.findAndMarkPendingBillingTask();
        if (task == null) return;
        executeTask(task);
    }

    private void executeTask(BillingTask task) {
        try {
            Class<?> clazz = Class.forName(task.getScriptClass());
            TaskScript script = (TaskScript) clazz.getDeclaredConstructor().newInstance();
            script.execute(task.getParametersMap());

            task.setStatus("COMPLETED");
        } catch (Exception e) {
            handleRetry(task);
        } finally {
            repository.save(task);
        }
    }

    private void handleRetry(BillingTask task) {
        int attempt = task.getAttempt() + 1;
        if (attempt >= task.getMaxRetries()) {
            task.setStatus("FAILED");
            return;
        }

        RetryStrategy strategy = getRetryStrategy(task.getRetryStrategy());

        long delay = strategy.calculateDelay(task, attempt);

        task.setAttempt(attempt);
        task.setScheduledTime(task.getScheduledTime().plus(delay, java.time.temporal.ChronoUnit.MILLIS));
        task.setStatus("PENDING");
    }

    private RetryStrategy getRetryStrategy(String strategyName) {
        return switch (strategyName) {
            case "FIXED_DELAY" -> new FixedDelayStrategy();
            case "EXPONENTIAL" -> new ExponentialBackoffStrategy();
            default -> throw new IllegalArgumentException("Неизвестная стратегия: " + strategyName);
        };
    }

    @Override
    public Page<?> getTasksByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatus(status, pageable);
    }
}
