package com.beta.backend.repo;

import com.beta.backend.domain.model.ERole;
import com.beta.backend.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
