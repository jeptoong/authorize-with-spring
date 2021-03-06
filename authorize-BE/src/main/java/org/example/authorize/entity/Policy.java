package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Policy entity.
 */
@Entity
@Data
public class Policy extends Audit<String> {

    @Id
    @IdGenerator("POL")
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(
            name = "id_generator",
            strategy = "org.example.authorize.utils.generator.id.StringIdentifierGenerator"
    )
    @Size(max = 35)
    private String id;

    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Column
    private boolean readOnly;

    @OneToMany(mappedBy = "policy")
    private List<PolicyPermission> policyPermissions;

    @OneToMany(mappedBy = "policy")
    private List<PolicyCondition> policyConditions;

    @ManyToMany
    @JoinTable(
            name = "role_policy",
            joinColumns = @JoinColumn(name = "policy_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
