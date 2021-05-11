package com.beta.backend.dto;

import com.beta.backend.model.ERole;
import com.beta.backend.model.Role;
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
