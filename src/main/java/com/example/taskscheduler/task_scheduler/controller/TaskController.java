package com.example.taskscheduler.task_scheduler.controller;

import com.example.taskscheduler.task_scheduler.entity.ScheduledTask;
import com.example.taskscheduler.task_scheduler.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceFactory taskServiceFactory;

    @PostMapping("/{category}")
    public void createTask(@PathVariable String category, @RequestBody Map<String, Object> payload)  {
        TaskService service = taskServiceFactory.getServiceByCategory(category);
        service.scheduleTask(payload);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelTask(@RequestBody Long taskId,@RequestParam String category){
        try{
            TaskService service = taskServiceFactory.getServiceByCategory(category);
            boolean isCanceled = service.cancelTask(taskId);

            if (isCanceled){
                return ResponseEntity.ok("Задача с ID " + taskId + " отменена.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Задача с ID " + taskId + " не отменена.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при отмене задачи: " + e.getMessage());

        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(
            @PathVariable String status,
            @RequestParam String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            int pageIndex = Math.max(page - 1, 0);

            TaskService service = taskServiceFactory.getServiceByCategory(category);
            Page<?> tasks = service.getTasksByStatus(status, pageIndex, size);
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении задач: " + e.getMessage());
        }
    }


}
