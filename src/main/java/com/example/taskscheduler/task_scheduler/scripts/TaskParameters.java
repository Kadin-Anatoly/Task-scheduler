package com.example.taskscheduler.task_scheduler.scripts;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TaskParameters {
    private final Map<String, Object> parameters;

    private ObjectMapper objectMapper = new ObjectMapper();

    public TaskParameters(Map<String, Object> parameters) {
        this.parameters = new HashMap<>(parameters);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = parameters.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Поле " + key + " не найдено");
        }
        if (!type.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Поле " + key + " имеет неверный тип");
        }
        return type.cast(value);
    }

    public String getString(String key) { return get(key, String.class); }

    public Integer getInt(String key) { return get(key, Integer.class); }

    public Long getLong(String key) {
        try {
            return objectMapper.convertValue(parameters.get(key), Long.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Поле " + key + " имеет неверный тип");
        }
    }
    public Boolean getBoolean(String key) { return get(key, Boolean.class); }

    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(parameters);
    }

    public String getParameters() {
        return parameters.toString();
    }
}


