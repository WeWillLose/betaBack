package com.beta.backend.dto;

import com.beta.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * A DTO representing a user, with only the public attributes.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class UserDTO{

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String middleName;

    private UserDTO chairman;

    private Set<Role> roles = new HashSet<>();


}
