package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.entity.BillingTask;
import com.example.taskscheduler.task_scheduler.entity.NotificationTask;
import com.example.taskscheduler.task_scheduler.entity.ReportingTask;
import com.example.taskscheduler.task_scheduler.repository.BillingTaskRepository;
import com.example.taskscheduler.task_scheduler.repository.NotificationTaskRepository;
import com.example.taskscheduler.task_scheduler.repository.ReportingTaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskLockService {

    @Autowired
    BillingTaskRepository billingRepository;
    @Autowired
    NotificationTaskRepository notificationRepository;
    @Autowired
    ReportingTaskRepository reportingRepository;

    @Transactional
    public BillingTask findAndMarkPendingBillingTask() {
        Long taskId = billingRepository.fetchNextPendingTaskId();
        if (taskId == null) return null;

        BillingTask task =  billingRepository.findTaskById(taskId);
        task.setStatus("RUNNING");
        billingRepository.save(task);
        return task;
    }

    @Transactional
    public NotificationTask findAndMarkPendingNotificationTask() {
        Long taskId = notificationRepository.fetchNextPendingTaskId();
        if (taskId == null) return null;

        NotificationTask task = notificationRepository.findTaskById(taskId);
        task.setStatus("RUNNING");
        notificationRepository.save(task);
        return task;
    }

    @Transactional
    public ReportingTask findAndMarkPendingReportingTask() {
        Long taskId = reportingRepository.fetchNextPendingTaskId();
        if (taskId == null) return null;

        ReportingTask task = reportingRepository.findTaskById(taskId);
        task.setStatus("RUNNING");
        reportingRepository.save(task);
        return task;
    }

}
