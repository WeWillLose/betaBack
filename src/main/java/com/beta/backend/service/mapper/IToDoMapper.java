package com.beta.backend.service.mapper;

import com.beta.backend.model.ToDo;
import com.beta.backend.dto.ToDoDTO;

import java.util.List;

public interface IToDoMapper {
    List<ToDoDTO> toDoToToDoDTOs(List<ToDo> toDos);

    ToDoDTO toDoToToDoDTO(ToDo toDo);

    List<ToDo> toDoDTOsToToDo(List<ToDoDTO> toDoDTOs);

    ToDo toDoDTOToToDo(ToDoDTO toDoDTO);
}
