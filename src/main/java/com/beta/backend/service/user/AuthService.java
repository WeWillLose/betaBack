package com.beta.backend.service.user;

import com.beta.backend.dto.UserDTO;
import com.beta.backend.model.User;
import com.beta.backend.dto.LoginDTO;
import com.beta.backend.dto.SignupDTO;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AuthService {
    UserDTO generateAuthUserDtoWithToken(User user, String token);

    User authenticateUser(@NonNull LoginDTO loginDTO);

    @PreAuthorize("hasRole('ADMIN')")
    User registerUser(@NonNull SignupDTO signUpDTO);

}
