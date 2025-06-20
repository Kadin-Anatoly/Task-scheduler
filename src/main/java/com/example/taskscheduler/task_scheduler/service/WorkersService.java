package com.example.taskscheduler.task_scheduler.service;

import com.example.taskscheduler.task_scheduler.workers.TaskWorker;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class WorkersService {

    private final ApplicationContext context;
    private final Map<String, ExecutorService> workerPools = new ConcurrentHashMap<>();

    public WorkersService(ApplicationContext context) {
        this.context = context;
    }

    public synchronized void initCategory(String category, int threads) {
        // Если пул уже существует - ничего не делаем
        if (workerPools.containsKey(category)) {
            return;
        }

        // Создаем новый пул потоков
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            TaskWorker worker = context.getBean(TaskWorker.class);
            worker.setCategory(category);
            pool.submit(worker);
        }

        // Сохраняем пул в мапу
        workerPools.put(category, pool);
    }

    /**
     * Останавливает все потоки категории
     */
    public synchronized void shutdownCategory(String category) {
        ExecutorService pool = workerPools.remove(category);
        if (pool != null) {
            pool.shutdownNow();
        }
    }
}
