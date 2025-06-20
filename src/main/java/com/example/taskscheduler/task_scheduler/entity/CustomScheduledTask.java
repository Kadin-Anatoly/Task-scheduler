package com.example.taskscheduler.task_scheduler.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "scheduled_tasks")
public class CustomScheduledTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String scriptClass;

    @Lob
    @Column(nullable = false)
    private String parameters;

    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    @Column(nullable = false)
    private String status; // PENDING, RUNNING, COMPLETED, FAILED, CANCELLED.

    private Integer attempt = 0;

    private Integer maxRetries;

    private String retryStrategy;

    private Integer retryDelayMs;

    private Double retryBase = Math.E;

    private Integer retryMaxDelayMs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScriptClass() {
        return scriptClass;
    }

    public void setScriptClass(String scriptClass) {
        this.scriptClass = scriptClass;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getRetryStrategy() {
        return retryStrategy;
    }

    public void setRetryStrategy(String retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    public Integer getRetryDelayMs() {
        return retryDelayMs;
    }

    public void setRetryDelayMs(Integer retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }

    public Integer getRetryMaxDelayMs() {
        return retryMaxDelayMs;
    }

    public void setRetryMaxDelayMs(Integer retryMaxDelayMs) {
        this.retryMaxDelayMs = retryMaxDelayMs;
    }

    public Map<String, Object> getParametersMap() throws Exception {
        return new ObjectMapper().readValue(parameters, Map.class);
    }

    public Double getRetryBase() {
        return retryBase;
    }

    public void setRetryBase(Double retryBase) {
        this.retryBase = retryBase;
    }
}
