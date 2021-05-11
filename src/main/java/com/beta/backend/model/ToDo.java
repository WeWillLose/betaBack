package com.beta.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@Entity
@Table(name = "todo")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"author"})
@EqualsAndHashCode(exclude = {"author"}, callSuper = false)
@SuperBuilder
public class ToDo extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String text;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;
}
