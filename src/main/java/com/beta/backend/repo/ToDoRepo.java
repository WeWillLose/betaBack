package com.beta.backend.repo;

import com.beta.backend.model.ToDo;
import com.beta.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepo extends JpaRepository<ToDo,Long> {
    List<ToDo> findByAuthor(User user);
}
