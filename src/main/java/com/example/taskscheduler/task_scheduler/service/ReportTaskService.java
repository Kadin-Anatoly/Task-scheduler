package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.entity.BillingTask;
import com.example.taskscheduler.task_scheduler.entity.ReportingTask;
import com.example.taskscheduler.task_scheduler.entity.ScheduledTask;
import com.example.taskscheduler.task_scheduler.repository.ReportingTaskRepository;
import com.example.taskscheduler.task_scheduler.retry.ExponentialBackoffStrategy;
import com.example.taskscheduler.task_scheduler.retry.FixedDelayStrategy;
import com.example.taskscheduler.task_scheduler.retry.RetryStrategy;
import com.example.taskscheduler.task_scheduler.scripts.TaskScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReportTaskService implements TaskService {

    @Autowired
    private TaskLockService taskLockService;

    private final ReportingTaskRepository repository;

    ReportTaskService(ReportingTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void scheduleTask(Map<String, Object> parameters) {

        ReportingTask task = new ReportingTask();

        task.setScriptClass((String) parameters.get("scriptClass"));
        task.setParameters((String) parameters.get("parameters"));
        task.setScheduledTime(LocalDateTime.parse((String) parameters.get("scheduledTime")));
        task.setMaxRetries((Integer) parameters.get("maxRetries"));
        task.setRetryStrategy((String) parameters.get("retryStrategy"));
        task.setRetryDelayMs((Integer) parameters.get("retryDelayMs"));
        task.setRetryMaxDelayMs((Integer) parameters.get("retryMaxDelayMs"));
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

    @Override
    public void processTasks() {
        ReportingTask task = taskLockService.findAndMarkPendingReportingTask();
        if (task == null) return;
        executeTask(task);
    }

    private void executeTask(ReportingTask task) {
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

    private void handleRetry(ReportingTask task) {
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
}
