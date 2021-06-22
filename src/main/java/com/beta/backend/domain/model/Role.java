package com.beta.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Id
    private ERole name;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return "ROLE_"+name.name();
    }
}
