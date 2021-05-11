package com.beta.backend.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PasswordResetDTO {
    @NotBlank
    @Length(min = 4)
    @Pattern(regexp = "^\\S+$",message = "Пароль не может содержать пробелы")
    private String password;
}
