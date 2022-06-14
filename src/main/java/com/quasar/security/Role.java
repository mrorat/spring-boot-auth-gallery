package com.quasar.security;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    private ROLES role;
    private String name;
    @Column(name = "deleted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
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

    public Role(ROLES role) {
        this.roleid = UUID.randomUUID().toString();
        this.role = role;
        this.name = role.name();
    }

    public String getName() {
        if (this.role == null) {
            this.role = (ROLES)Enum.valueOf(ROLES.class, this.name);
        }

        return this.role.name();
    }
}
