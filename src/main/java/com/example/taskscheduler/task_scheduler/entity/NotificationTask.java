package com.example.taskscheduler.task_scheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "scheduled_task_notification")
public class NotificationTask extends ScheduledTask {}
