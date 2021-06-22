package com.beta.backend.domain.dto;

import com.beta.backend.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;


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

    private String token;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
