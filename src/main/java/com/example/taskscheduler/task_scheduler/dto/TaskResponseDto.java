package com.example.taskscheduler.task_scheduler.dto;

import com.example.taskscheduler.task_scheduler.entity.CustomScheduledTask;
import java.time.LocalDateTime;

public record TaskResponseDto(
        Long id,
        String category,
        String status,
        LocalDateTime scheduledTime,
        int attempt
) {
    public static TaskResponseDto fromEntity(CustomScheduledTask task) {
        return new TaskResponseDto(
                task.getId(),
                task.getCategory(),
                task.getStatus(),
                task.getScheduledTime(),
                task.getAttempt()
        );
    }
}
