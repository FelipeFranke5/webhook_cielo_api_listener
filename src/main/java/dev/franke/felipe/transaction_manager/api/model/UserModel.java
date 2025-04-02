package dev.franke.felipe.transaction_manager.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "authentication")
@Schema(description = "User")
public class UserModel implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "The User Id", example = "1")
    private Long id;

    @Column(name = "name")
    @Schema(description = "User Name", example = "myUserName123")
    private String name;

    @Column(name = "password")
    @Schema(description = "Password stored")
    private String password;

    @Column(name = "email")
    @Schema(description = "User Email Address", example = "youremail@email.com")
    private String email;

    @Column(name = "active")
    @Schema(description = "If the User is active and can perform authentication")
    private Boolean active = false;

    @Column(name = "created_at")
    @Schema(description = "Creation of User Timestamp")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "role")
    @Schema(description = "User Role", example = "USER")
    private String role;

    protected UserModel() {}

    public UserModel(
            final Long id,
            final String name,
            final String password,
            final String email,
            final Boolean active,
            final Instant createdAt,
            final String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.active = active;
        this.createdAt = createdAt;
        this.role = role;
    }

    public UserModel(
            final String name,
            final String password,
            final String email,
            final Boolean active,
            final Instant createdAt,
            final String role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.active = active;
        this.createdAt = createdAt;
        this.role = role;
    }

    public UserModel(final String name, final String password, final String email, final String role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final UserModel other = (UserModel) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (password == null) {
            if (other.password != null) return false;
        } else if (!password.equals(other.password)) return false;
        if (email == null) {
            if (other.email != null) return false;
        } else if (!email.equals(other.email)) return false;
        if (active == null) {
            if (other.active != null) return false;
        } else if (!active.equals(other.active)) return false;
        if (createdAt == null) {
            return other.createdAt == null;
        } else return createdAt.equals(other.createdAt);
    }

    @Override
    public String toString() {
        return "UserModel [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", active="
                + active + ", createdAt=" + createdAt + "]";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final var userRole = new SimpleGrantedAuthority("ROLE_USER");
        final var adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
        final var adminAuthorities = List.of(adminRole, userRole);
        final var userAuthorities = List.of(userRole);
        final var isUserAdmin = this.getRole().equals("ADMIN");
        return isUserAdmin ? adminAuthorities : userAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    @Override
    public void eraseCredentials() {
        this.setPassword(null);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
