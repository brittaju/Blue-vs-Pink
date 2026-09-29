package brittaju.datasource.model;

import brittaju.domain.model.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "login"))
public class UserEntity {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "login", nullable = false, length = 50, unique = true)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private List<Role> roles = new ArrayList<>();

    protected UserEntity() {}

    public UserEntity(UUID id, String login, String passwordHash) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public UUID getId() { return id; }

    public String getLogin() { return login; }

    public String getPasswordHash() { return passwordHash; }

    public List<Role> getRoles() { return roles; }

    public void setRoles(List<Role> roles) { this.roles = roles; }

}