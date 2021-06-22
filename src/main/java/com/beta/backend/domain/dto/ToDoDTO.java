package com.beta.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class ToDoDTO extends AuditBaseModel {

    private Long id;

    private String title;

    private String description;

    private String text;

    private UserDTO author;
}
