package com.beta.backend.service.user;

import com.beta.backend.domain.dto.UserDTO;
import com.beta.backend.domain.model.User;
import com.beta.backend.domain.dto.LoginDTO;
import com.beta.backend.domain.dto.SignupDTO;
import lombok.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AuthService {
    UserDTO generateAuthUserDtoWithToken(User user, String token);

    User authenticateUser(@NonNull LoginDTO loginDTO);

    @PreAuthorize("hasRole('ADMIN')")
    User registerUser(@NonNull SignupDTO signUpDTO);

}
