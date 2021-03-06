package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Principal entity.
 */
@Entity
@Data
public class Principal extends Audit<String> {

    @Id
    @IdGenerator("PRI")
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(
            name = "id_generator",
            strategy = "org.example.authorize.utils.generator.id.StringIdentifierGenerator"
    )
    @Size(max = 35)
    private String id;

    @NotNull
    @Column(nullable = false)
    private boolean disabled;

    @NotNull
    @Column(nullable = false)
    private boolean deleted;

    @Column
    private LocalDateTime expireDate;

    @Column
    private boolean locked;

    @OneToOne(mappedBy = "principal")
    private Account account;

    @OneToOne(mappedBy = "principal")
    private PrincipalAttempt principalAttempt;

    @ManyToMany
    @JoinTable(
            name = "principal_role",
            joinColumns = @JoinColumn(name = "principal_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "principal", cascade = CascadeType.PERSIST)
    private List<AuthMethod> authMethods;
}
