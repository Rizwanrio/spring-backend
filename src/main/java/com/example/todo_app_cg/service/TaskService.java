package com.example.todo_app_cg.service;



import com.example.todo_app_cg.controller.NotificationController;
import com.example.todo_app_cg.model.Task;
import com.example.todo_app_cg.model.User;
import com.example.todo_app_cg.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;



@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationController notificationController;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(User user) {
        System.out.println("Fetching tasks for user: " + user.getUsername());

        return taskRepository.findByUser(user);
    }


    public Task getTaskById(Long id, User user) {
        return taskRepository.findByIdAndUser(id, user).orElse(null); // Returns null if the task is not found
    }


    public Task updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id).orElseThrow();
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        existingTask.setCategory(task.getCategory());
        existingTask.setStatus(task.getStatus());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }


    // Method to update the status of overdue tasks
    @Scheduled(cron = "0 */10 * * * ?") // Runs every day at midnight
    public void updateOverdueTasks() {
        LocalDate today = LocalDate.now();
        System.out.println("hi");
        List<Task> overdueTasks = taskRepository.findPendingTasksWithPastDueDate(today);

        for (Task task : overdueTasks) {
            task.setStatus("overdue");
            taskRepository.save(task);
            // Optionally, trigger a notification for each overdue task
            // notifyUser(task);
System.out.println("hello");
            // Send notification to clients about overdue task
            String notificationMessage = "Task '" + task.getTitle() + "' is overdue!";
            notificationController.sendOverdueTaskNotification(notificationMessage);
        }
    }






}

