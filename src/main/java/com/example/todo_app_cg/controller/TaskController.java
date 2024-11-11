package com.example.todo_app_cg.controller;

import com.example.todo_app_cg.model.Task;
import com.example.todo_app_cg.model.User;
import com.example.todo_app_cg.repository.UserRepository;
import com.example.todo_app_cg.security.CustomUserDetails;
import com.example.todo_app_cg.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        // Manually retrieve the authenticated user from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("im 403 forbidden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 if no user is authenticated
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        task.setUser(user);
        System.out.println("Creating task with due date: " + task.getDueDate());
        return ResponseEntity.ok(taskService.createTask(task));
    }


    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = customUserDetails.getUser();
            return ResponseEntity.ok(taskService.getAllTasks(user));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){

        return (CsrfToken) request.getAttribute("_csrf");

    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Task task = taskService.getTaskById(id, user);
        System.out.println("these are user and task " + user + task);

        if (task == null) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the task doesn't exist
        }
        return ResponseEntity.ok(task);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
