package com.example.todo_app_cg.repository;



import com.example.todo_app_cg.model.Task;
import com.example.todo_app_cg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    Optional<Task> findByIdAndUser(Long id, User user);

    @Query("SELECT t FROM Task t WHERE t.dueDate < :today AND t.status = 'pending'")
    List<Task> findPendingTasksWithPastDueDate(LocalDate today);
}





