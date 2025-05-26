package com.example.taskscheduler.task_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class TaskSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskSchedulerApplication.class, args);
	}
}
