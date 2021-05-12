package com.beta.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = {"reports","toDos","chairman"},callSuper = false)
@ToString(exclude = {"reports","toDos","chairman"})
@Entity
@Data
@Table(name = "usr")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends AbstractAuditingEntity implements UserDetails, Serializable {

    static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator",initialValue = 2,allocationSize = 5,sequenceName = "user_id_sequence")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private String middleName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "chairman_id")
    @JsonIgnore
    private User chairman;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    private Set<Role> roles;

    @OneToMany (mappedBy="author", fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private Set<Report> reports = new HashSet<>();

    @OneToMany (mappedBy="author", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private Set<ToDo> toDos = new HashSet<>();

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getIsActive();
    }

    @Override
    public boolean isEnabled() {
        return getIsActive();
    }
}
