package com.quasar.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(
    name = "user"
)
public class User implements UserDetails {

	private static final long serialVersionUID = 8838272408963791948L;
	@Id
    @Column(
        length = 36,
        unique = true,
        nullable = false
    )
    private String userid;
    private String username;
    private String password;
    private boolean enabled;
    @ManyToMany(
        fetch = FetchType.EAGER,
        cascade = {CascadeType.ALL}
    )
    @JoinTable(
        name = "users_roles",
        joinColumns = {@JoinColumn(
    name = "userid"
)},
        inverseJoinColumns = {@JoinColumn(
    name = "roleid"
)}
    )
    private Set<Role> roles;

    public User() {
        this.roles = new HashSet<>();
        this.userid = UUID.randomUUID().toString();
    }

    public User(Collection<Role> roles) {
        this();
        this.roles.addAll(roles);
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAccountNonExpired() {
        return this.isEnabled();
    }

    public boolean isAccountNonLocked() {
        return this.isEnabled();
    }

    public boolean isCredentialsNonExpired() {
        return this.isEnabled();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        Iterator<?> var2 = this.getRoles().iterator();

        while(var2.hasNext()) {
            Role role = (Role)var2.next();
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
            authorities.add(grantedAuthority);
        }

        return authorities;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = (new MyPasswordEncoder()).encode(password);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    Set<Role> getRoles() {
        return this.roles;
    }

    void setRoles(ROLES... roles) {
        ROLES[] var2 = roles;
        int var3 = roles.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ROLES role = var2[var4];
            this.roles.add(new Role(role));
        }

    }

    public String getID() {
        return this.userid;
    }

    public boolean hasRole(ROLES role) {
        return this.roles.stream().filter((r) -> {
            return r.getName().equals(role.name());
        }).findFirst().isPresent();
    }
}
