package com.beta.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String middleName;
}
