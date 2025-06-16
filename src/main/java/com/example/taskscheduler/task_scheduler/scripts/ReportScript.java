package com.example.taskscheduler.task_scheduler.scripts;

public class ReportScript implements TaskScript{
    @Override
    public void execute(TaskParameters parameters) throws Exception {
        Long userId = parameters.getLong("userId");
        Integer amount = parameters.getInt("amount");
        Thread.sleep(10000);
        System.out.println("Выполняю reportScript для пользователя ID=" + userId + ", сумма: " + amount);
    }
}
