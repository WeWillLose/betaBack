package com.beta.backend.service.user.impl;

import com.beta.backend.service.user.UserValidationService;
import org.springframework.stereotype.Service;

@Service
public class UserValidationServiceImpl implements UserValidationService {

    @Override
    public boolean validateUserPassword(String password){
        return password != null && !password.isBlank() && password.length() >= 6;
    }
    @Override
    public boolean validateUserUsername(String username){
        return username != null && !username.isBlank() && username.length() >= 4;
    }

    @Override
    public boolean validateUserFirstName(String firstName) {
        return firstName != null && !firstName.isBlank();
    }

    @Override
    public boolean validateUserLastName(String lastName) {
        return lastName != null && !lastName.isBlank();
    }

    @Override
    public boolean validateUserPatronymic(String patronymic) {
        return patronymic != null && !patronymic.isBlank();
    }
}
