package com.beta.backend.domain.dto;

import com.beta.backend.domain.model.ERole;
import com.beta.backend.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NonNull
    private ERole name;

    public Role toRole(){
        return name==null?null:new Role(name);
    }
}
