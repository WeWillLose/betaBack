package com.beta.backend.service.mapper;

import com.beta.backend.domain.model.User;
import com.beta.backend.domain.dto.UserDTO;

import java.util.List;

public interface UserMapper {
    List<UserDTO> usersToUserDTOs(List<User> users);

    UserDTO userToUserDTOWithToken(User user,String token);

    UserDTO userToUserDTO(User user);

    List<User> userDTOsToUsers(List<UserDTO> userDTOs);

    User userDTOToUser(UserDTO userDTO);

    User userFromId(Long id);
}
