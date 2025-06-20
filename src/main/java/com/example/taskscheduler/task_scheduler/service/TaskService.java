package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.entity.CustomScheduledTask;
import com.example.taskscheduler.task_scheduler.repository.TasksRepository;
import com.example.taskscheduler.task_scheduler.retry.ExponentialBackoffStrategy;
import com.example.taskscheduler.task_scheduler.retry.FixedDelayStrategy;
import com.example.taskscheduler.task_scheduler.retry.RetryStrategy;
import com.example.taskscheduler.task_scheduler.scripts.TaskParameters;
import com.example.taskscheduler.task_scheduler.scripts.TaskScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class TaskService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WorkersService workersService;

    @Autowired
    private TaskLockService taskLockService;

    private final TasksRepository repository;

    private Map<String, Object> parseParameters(String parameters) throws JsonProcessingException {
        return objectMapper.readValue(parameters, Map.class); // Преобразование JSON в Map
    }


    public TaskService(TasksRepository repository) {
        this.repository = repository;
    }


    public void scheduleTask(Map<String, Object> payload) throws JsonProcessingException {

        CustomScheduledTask task = new CustomScheduledTask();

        task.setScriptClass((String) payload.get("scriptClass"));
        task.setParameters(objectMapper.writeValueAsString(payload.get("parameters"))); // Преобразование Map в JSON
        task.setScheduledTime(LocalDateTime.parse((String) payload.get("scheduledTime")));
        task.setMaxRetries((Integer) payload.get("maxRetries"));
        task.setRetryStrategy((String) payload.get("retryStrategy"));
        task.setRetryDelayMs((Integer) payload.get("retryDelayMs"));
        task.setRetryMaxDelayMs((Integer) payload.get("retryMaxDelayMs"));
        task.setCategory((String) payload.get("category"));
        task.setStatus("PENDING");
        task.setAttempt(0);

        workersService.initCategory(task.getCategory(), 1);

        repository.save(task);
    }

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

    public void processTasks(String category) {
        CustomScheduledTask task = taskLockService.findAndMarkPendingTaskByCategory(category);
        if (task == null) return;
        executeTask(task, category);
    }

    private void executeTask(CustomScheduledTask task, String category) {
        Thread currentThread = Thread.currentThread();
        System.out.println("Задача ID = " + task.getId() + " с категорией: " + category + " выполняется в потоке: " + currentThread.getName() + " (ID: " + currentThread.getId() + ")");

        try {
            Class<?> clazz = Class.forName(task.getScriptClass());
            TaskScript script = (TaskScript) clazz.getDeclaredConstructor().newInstance();
            Map<String, Object> parametersMap = parseParameters(task.getParameters());
            TaskParameters parameters = new TaskParameters(parametersMap);

            try {
                script.execute(parameters);
            } catch (Exception e) {
                System.err.println("Ошибка при выполнении скрипта: " + e.getMessage());
                e.printStackTrace();
                handleRetry(task);
            }

            task.setStatus("COMPLETED");
        } catch (Exception e) {
            handleRetry(task);
        } finally {
            repository.save(task);
        }
    }

    private void handleRetry(CustomScheduledTask task) {
        int attempt = task.getAttempt() + 1;
        if (attempt >= task.getMaxRetries()) {
            task.setStatus("FAILED");
            return;
        }

        RetryStrategy strategy = getRetryStrategy(task.getRetryStrategy());

        long delay = strategy.calculateDelay(task, attempt);

        task.setAttempt(attempt);
        task.setScheduledTime(task.getScheduledTime().plus(delay, ChronoUnit.MILLIS));
        task.setStatus("PENDING");
    }

    private RetryStrategy getRetryStrategy(String strategyName) {
        return switch (strategyName) {
            case "FIXED_DELAY" -> new FixedDelayStrategy();
            case "EXPONENTIAL" -> new ExponentialBackoffStrategy();
            default -> throw new IllegalArgumentException("Неизвестная стратегия: " + strategyName);
        };
    }

    public Page<?> getTasksByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatus(status, pageable);
    }
}



