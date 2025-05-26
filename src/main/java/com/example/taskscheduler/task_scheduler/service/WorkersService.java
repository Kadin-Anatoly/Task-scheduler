package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.workers.BillingWorker;
import com.example.taskscheduler.task_scheduler.workers.NotificationWorker;
import com.example.taskscheduler.task_scheduler.workers.ReportWorker;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@RequiredArgsConstructor
public class WorkersService implements CommandLineRunner {


    private final NotificationWorker notificationWorker;
    private final ReportWorker reportWorker;
    private final BillingTaskService billingTaskService;

    @Setter
    private int billingPoolSize = 3;
    @Setter
    private int notificationPoolSize = 3;
    @Setter
    private int reportingPoolSize = 3;

    private ExecutorService billingExecutor;
    private ExecutorService notificationExecutor;
    private ExecutorService reportingExecutor;

    @Override
    public void run(String... args) throws Exception {
        startAllWorkers();
    }

    public void startAllWorkers() {
        shutdownExistingPools();

        billingExecutor = Executors.newFixedThreadPool(billingPoolSize);
        for (int i = 0; i < billingPoolSize; i++) {
            System.out.println("Starting worker " + i);
            billingExecutor.submit(new BillingWorker(billingTaskService));
        }

        notificationExecutor = Executors.newFixedThreadPool(notificationPoolSize);
        for (int i = 0; i < notificationPoolSize; i++) {
            notificationExecutor.submit(notificationWorker);
        }

        reportingExecutor = Executors.newFixedThreadPool(reportingPoolSize);
        for (int i = 0; i < reportingPoolSize; i++) {
            reportingExecutor.submit(reportWorker);
        }

        System.out.println("Все пулы задач запущены.");
    }

    public void stopAllWorkers() {
        shutdownExecutor(billingExecutor, "Billing");
        shutdownExecutor(notificationExecutor, "Notification");
        shutdownExecutor(reportingExecutor, "Reporting");

        System.out.println("Все пулы задач остановлены.");
    }

    private void shutdownExecutor(ExecutorService executor, String name) {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
            System.out.println("Пул " + name + " остановлен.");
        }
    }

    private void shutdownExistingPools() {
        shutdownExecutor(billingExecutor, "Billing");
        shutdownExecutor(notificationExecutor, "Notification");
        shutdownExecutor(reportingExecutor, "Reporting");

        System.out.println("Существующие пулы задач остановлены.");
    }
}
