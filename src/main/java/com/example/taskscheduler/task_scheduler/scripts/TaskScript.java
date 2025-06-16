package com.example.taskscheduler.task_scheduler.scripts;

import java.util.Map;

public interface TaskScript {
    void execute(TaskParameters parameters) throws Exception;
}

