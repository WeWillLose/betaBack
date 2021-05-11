package com.beta.backend.env;

import com.beta.backend.model.ERole;
import com.beta.backend.model.Role;

public final class ROLES {
    private ROLES(){}
    public static Role ADMIN = new Role(ERole.ADMIN);
    public static Role TEACHER = new Role(ERole.TEACHER);
    public static Role CHAIRMAN = new Role(ERole.CHAIRMAN);
}
