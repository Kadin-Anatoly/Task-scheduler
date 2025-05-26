package com.example.taskscheduler.task_scheduler.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;


@MappedSuperclass
public class ScheduledTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scriptClass;
    private String parameters; // JSON строка


    private LocalDateTime scheduledTime;

    private String status = "PENDING"; // PENDING, RUNNING, COMPLETED, CANCELLED

    private Integer maxRetries = 0;
    private String retryStrategy; // NONE, FIXED_DELAY, EXPONENTIAL
    private Integer retryDelayMs;
    private Double retryBase = Math.E;
    private Integer retryMaxDelayMs;

    private Integer attempt = 0;

    public Map<String, Object> getParametersMap() throws Exception {
        return new ObjectMapper().readValue(parameters, Map.class);
    }

    //Геттеры и сеттеры:

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getRetryBase() {
        return retryBase;
    }

    public void setRetryBase(Double retryBase) {
        this.retryBase = retryBase;
    }

    public Integer getRetryMaxDelayMs() {
        return retryMaxDelayMs;
    }

    public void setRetryMaxDelayMs(Integer retryMaxDelayMs) {
        this.retryMaxDelayMs = retryMaxDelayMs;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }
}
