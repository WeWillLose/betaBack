package com.beta.backend.service.mapper;

import com.beta.backend.model.User;
import com.beta.backend.dto.UserDTO;

import java.util.List;

public interface IUserMapper {
    List<UserDTO> usersToUserDTOs(List<User> users);

    UserDTO userToUserDTOWithToken(User user,String token);

    UserDTO userToUserDTO(User user);

    List<User> userDTOsToUsers(List<UserDTO> userDTOs);

    User userDTOToUser(UserDTO userDTO);

    User userFromId(Long id);
}
