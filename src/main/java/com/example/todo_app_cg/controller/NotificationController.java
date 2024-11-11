package com.example.todo_app_cg.controller;

import com.example.todo_app_cg.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class NotificationController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/notifications/overdue-tasks")
    public SseEmitter getOverdueTaskNotifications() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);

        // Remove the emitter when it's completed or timed out
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    public void sendOverdueTaskNotification(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("overdueTaskNotification").data(message));
            } catch (IOException e) {
                emitters.remove(emitter); // Remove failed emitters
            }
        }
    }
}
