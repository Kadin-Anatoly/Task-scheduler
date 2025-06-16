package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.workers.TaskWorker;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WorkersService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WorkersService.class);

    private final Map<String, TaskWorker> taskWorkers; // Карта воркеров по категориям
    private final Map<String, ExecutorService> workerPools = new ConcurrentHashMap<>(); // Карта пулов воркеров

    @Setter
    private int poolSize = 2;

    public WorkersService(Map<String, TaskWorker> taskWorkers) {
        this.taskWorkers = taskWorkers;
    }

    @Override
    public void run(String... args) throws Exception {
        startAllWorkers();
    }

    public void startAllWorkers() {
        taskWorkers.forEach((category, worker) -> {
            shutdownExistingPool(category);
            ExecutorService pool = Executors.newFixedThreadPool(poolSize);
            for (int i = 0; i < poolSize; i++) {
                pool.submit(worker);
            }
            workerPools.put(category, pool);
            System.out.printf("Пул воркеров для категории %s запущен.", category);
        });
    }

    public void stopAllWorkers() {
        workerPools.forEach((category, pool) -> {
            shutdownExecutor(pool, category);
            System.out.printf("Пул воркеров для категории %s остановлен.", category);
        });
        workerPools.clear();
    }

    private void shutdownExecutor(ExecutorService executor, String category) {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }

    private void shutdownExistingPool(String category) {
        ExecutorService existingPool = workerPools.get(category);
        if (existingPool != null && !existingPool.isShutdown()) {
            existingPool.shutdownNow();
            logger.info("Существующий пул воркеров для категории {} остановлен.", category);
        }
    }
}
