package com.beta.backend.service.mapper;

import com.beta.backend.domain.model.ToDo;
import com.beta.backend.domain.dto.ToDoDTO;

import java.util.List;

public interface ToDoMapper {
    List<ToDoDTO> toDoToToDoDTOs(List<ToDo> toDos);

    ToDoDTO toDoToToDoDTO(ToDo toDo);

    List<ToDo> toDoDTOsToToDo(List<ToDoDTO> toDoDTOs);

    ToDo toDoDTOToToDo(ToDoDTO toDoDTO);
}
