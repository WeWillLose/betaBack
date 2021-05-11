package com.beta.backend.service.user;

public interface UserValidationService {
    boolean validateUserPassword(String password);

    boolean validateUserUsername(String username);

    boolean validateUserFirstName(String firstName);

    boolean validateUserLastName(String lastName);

    boolean validateUserPatronymic(String patronymic);
}
