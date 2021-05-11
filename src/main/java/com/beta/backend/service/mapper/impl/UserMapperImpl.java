package com.beta.backend.service.mapper.impl;

import com.beta.backend.model.User;
import com.beta.backend.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserMapperImpl implements com.beta.backend.service.mapper.IUserMapper {

    @Override
    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
    }

    private UserDTO chairmanToUserDTOs(User user) {
        return user == null ? null : UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

    @Override
    public UserDTO userToUserDTO(User user) {
        return user == null ? null :
                UserDTO.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .middleName(user.getMiddleName())
                        .username(user.getUsername())
                        .roles(user.getRoles())
                        .id(user.getId())
                        .chairman(chairmanToUserDTOs(user.getChairman()))
                        .build();
    }


    @Override
    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        return userDTO == null ? null : User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .lastName(userDTO.getLastName())
                .firstName(userDTO.getFirstName())
                .middleName(userDTO.getMiddleName())
                .roles(userDTO.getRoles())
                .build();
    }


    @Override
    public User userFromId(Long id) {
        User user = new User();
        user.setId(id);
        return id == null ? null : User.builder()
                .id(id)
                .build();

    }
}
