package com.beta.backend.service.toDo.impl;


import com.beta.backend.model.ToDo;
import com.beta.backend.model.User;
import com.beta.backend.repo.ToDoRepo;
import com.beta.backend.exception.impl.ForbiddenExceptionImpl;
import com.beta.backend.exception.impl.ToDoNotFoundExceptionImpl;
import com.beta.backend.exception.impl.UserNotFoundExceptionImpl;
import com.beta.backend.service.toDo.ToDoService;
import com.beta.backend.service.user.UserService;
import com.beta.backend.utils.SecurityUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepo toDoRepository;
    private final UserService userService;

    @Override
    public  List<ToDo> findByAuthorId(long authorId) {
        User byId = userService.findById(authorId).orElse(null);

        if(byId == null){
            throw new UserNotFoundExceptionImpl(authorId);
        }

        List<ToDo> reportsByAuthor = toDoRepository.findByAuthor(byId);

        if(reportsByAuthor == null){
            log.error("IN findByAuthorId reportsByAuthor is null");
            throw new NullPointerException("IN findByAuthorId reportsByAuthor is null");
        }
        log.info("IN findByAuthorId found: {} by author {}",reportsByAuthor,byId);
        return reportsByAuthor;
    }

    @Override
    public ToDo findById(long id){
        return toDoRepository.findById(id).orElse(null);
    }

    @Override
    public ToDo editToDo(long sourceToDoId,@NonNull ToDo changedToDo) {
        ToDo toDo = findById(sourceToDoId);

        if(toDo==null){
            throw new ToDoNotFoundExceptionImpl(sourceToDoId);
        }
        if(!SecurityUtils.isCurrentUserEqualsUserOrAdmin(toDo.getAuthor())){
            throw new ForbiddenExceptionImpl();
        }

        if(changedToDo.getTitle() != null && !changedToDo.getTitle().isBlank()){
            toDo.setTitle(changedToDo.getTitle());
        }

        if(changedToDo.getText() !=null && !changedToDo.getText().isBlank()){
            toDo.setText(changedToDo.getText());
        }

        if(changedToDo.getDescription() !=null && !changedToDo.getDescription().isBlank()){
            toDo.setDescription(changedToDo.getDescription());
        }

        return toDoRepository.save(toDo);
    }

    @Override
    public ToDo addToDo(long authorId,@NonNull ToDo toDo) throws UserNotFoundExceptionImpl {
        User byId = userService.findById(authorId).orElse(null);

        if(byId == null){
            throw new UserNotFoundExceptionImpl(authorId);
        }
        toDo.setAuthor(byId);
        return  toDoRepository.save(toDo);
    }

    @Override
    public boolean existsById(long id){
        return toDoRepository.existsById(id);
    }

    @Override
    public void deleteToDo(long toDoId) throws ToDoNotFoundExceptionImpl, ForbiddenExceptionImpl {

        ToDo toDo = findById(toDoId);

        if(toDo == null){
            throw new ToDoNotFoundExceptionImpl(toDoId);
        }
        if(!SecurityUtils.isCurrentUserEqualsUserOrAdmin(toDo.getAuthor())){
            throw new ForbiddenExceptionImpl();
        }
        toDoRepository.delete(toDo);
    }
}
