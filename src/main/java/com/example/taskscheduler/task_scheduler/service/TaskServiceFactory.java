package com.example.taskscheduler.task_scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceFactory {

    private final BillingTaskService billingTaskService;
    private final NotificationTaskService notificationTaskService;
    private final ReportTaskService reportTaskService;

    public TaskServiceFactory(
            BillingTaskService billingTaskService,
            NotificationTaskService notificationTaskService,
            ReportTaskService reportTaskService
    ) {
        this.billingTaskService = billingTaskService;
        this.notificationTaskService = notificationTaskService;
        this.reportTaskService = reportTaskService;
    }

    public TaskService getServiceByCategory(String category) {
        return switch (category.toLowerCase()) {
            case "billing" -> billingTaskService;
            case "notification" -> notificationTaskService;
            case "reporting" -> reportTaskService;
            default -> throw new IllegalArgumentException("Неизвестная категория: " + category);
        };
    }
}
