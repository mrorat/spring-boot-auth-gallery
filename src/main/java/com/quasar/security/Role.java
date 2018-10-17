package com.quasar.security;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(
    name = "role"
)
public class Role {
    @Id
    @Column(
        length = 36,
        unique = true,
        nullable = false
    )
    private String roleid;
    @Transient
    private Roles role;
    private String name;
    @ManyToMany(
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "users_roles",
        joinColumns = {@JoinColumn(
    name = "roleid"
)},
        inverseJoinColumns = {@JoinColumn(
    name = "userid"
)}
    )
    private Set<User> users;

    public Role() {
    }

    public Role(Roles role) {
        this.roleid = UUID.randomUUID().toString();
        this.role = role;
        this.name = role.name();
    }

    public String getName() {
        if (this.role == null) {
            this.role = (Roles)Enum.valueOf(Roles.class, this.name);
        }

        return this.role.name();
    }
}
