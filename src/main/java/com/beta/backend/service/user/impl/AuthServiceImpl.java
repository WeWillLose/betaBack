package com.beta.backend.service.user.impl;

import com.beta.backend.dto.UserDTO;
import com.beta.backend.model.ERole;
import com.beta.backend.model.Role;
import com.beta.backend.model.User;
import com.beta.backend.repo.RoleRepo;
import com.beta.backend.dto.LoginDTO;
import com.beta.backend.dto.SignupDTO;
import com.beta.backend.exception.impl.UserNotFoundExceptionImpl;
import com.beta.backend.service.mapper.IUserMapper;
import com.beta.backend.service.user.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements com.beta.backend.service.user.AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleRepo roleRepo;
    private final IUserMapper userMapper;
    @Value("${jwt.AuthorizationHeaderName}")
    private String AuthorizationHeaderName;

    @Override
    public ResponseEntity<UserDTO> generateAuthResponseEntity(User user, String token) {
        return ResponseEntity.ok().header(AuthorizationHeaderName,token).body(userMapper.userToUserDTO(user));
    }

    @Override
    public User authenticateUser(@NonNull LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            log.info("IN authenticateUser user with username: {}, password : {} wos authenticated",
                    loginDTO.getUsername(), loginDTO.getPassword());
            return user;
        } catch (AuthenticationException e) {
            log.error("IN authenticateUser auth for user with login: {}, and password: {} failed", loginDTO.getUsername(), loginDTO.getPassword());
            throw new UserNotFoundExceptionImpl();
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public User registerUser(@NonNull SignupDTO signUpDTO) {
        log.info("IN registerUser signUpDTO: {}", signUpDTO);

        User userToSave = User.builder()
                .username(signUpDTO.getUsername())
                .password(signUpDTO.getPassword())
                .lastName(signUpDTO.getLastName())
                .firstName(signUpDTO.getFirstName())
                .middleName(signUpDTO.getMiddleName())
                .roles(Collections.singleton(new Role(ERole.TEACHER)))
                .isActive(true)
                .build();
        return userService.createUser(userToSave);
    }

    @Override
    public boolean initDb() {
        Set.of(new Role(ERole.TEACHER),new Role(ERole.ADMIN),new Role(ERole.CHAIRMAN)).forEach(roleRepo::save);
        User userToSave = User.builder()
                .username("admin")
                .password("admin")
                .lastName("Admin")
                .firstName("Admin")
                .middleName("Admin")
                .roles(Set.of(new Role(ERole.TEACHER),new Role(ERole.ADMIN),new Role(ERole.CHAIRMAN)))
                .isActive(true)
                .build();
        return userService.createUser(userToSave) != null;
    }
}
