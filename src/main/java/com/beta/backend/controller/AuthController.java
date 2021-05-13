package com.beta.backend.controller;

import com.beta.backend.dto.LoginDTO;
import com.beta.backend.dto.SignupDTO;
import com.beta.backend.dto.UserDTO;
import com.beta.backend.model.User;
import com.beta.backend.security.jwt.JwtTokenProvider;
import com.beta.backend.service.mapper.IUserMapper;
import com.beta.backend.service.user.AuthService;
import com.beta.backend.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final IUserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("authenticate")
    public UserDTO authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        User user = authService.authenticateUser(loginDTO);
        return authService.generateAuthUserDtoWithToken(user,jwtTokenProvider.generateToken(user.getUsername()));
    }

    @PostMapping("registration")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDTO signUpDTO) {
        return ResponseEntity.ok().body(userMapper.userToUserDTO(authService.registerUser(signUpDTO)));
    }

    @GetMapping("authenticate")
    public ResponseEntity<UserDTO> getcurrentUser() {
        return ResponseEntity.ok().body(userMapper.userToUserDTO(SecurityUtils.getCurrentUser()));
    }

}
