package com.example.taskscheduler.task_scheduler.repository;

import com.example.taskscheduler.task_scheduler.entity.BillingTask;
import com.example.taskscheduler.task_scheduler.entity.ReportingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportingTaskRepository extends JpaRepository<ReportingTask, Long> {
    @Query(value = """
    SELECT id
    FROM scheduled_task_reporting
    WHERE status = 'PENDING' AND scheduled_time <= NOW()
    ORDER BY scheduled_time ASC
    LIMIT 1
    FOR UPDATE SKIP LOCKED
""", nativeQuery = true)
    Long fetchNextPendingTaskId();

    @Query(value = """
        SELECT * FROM scheduled_task_reporting
        WHERE id = :id
    """, nativeQuery = true)
    ReportingTask findTaskById(@Param("id") Long id);
}
