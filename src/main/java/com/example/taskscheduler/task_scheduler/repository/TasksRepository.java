package com.example.taskscheduler.task_scheduler.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.taskscheduler.task_scheduler.entity.CustomScheduledTask;

@Repository
public interface TasksRepository extends JpaRepository<CustomScheduledTask,Long> {
    @Query(value = """
    SELECT id
    FROM scheduled_tasks
    WHERE status = 'PENDING' AND scheduled_time <= NOW()
    ORDER BY scheduled_time ASC
    LIMIT 1
    FOR UPDATE SKIP LOCKED
""", nativeQuery = true)
    Long fetchNextPendingTaskId();

    @Query(value = """
        SELECT * FROM scheduled_tasks
        WHERE id = :id
    """, nativeQuery = true)
    CustomScheduledTask findTaskById(@Param("id") Long id);

    Page<CustomScheduledTask> findByStatus(String status, Pageable pageable);
}
