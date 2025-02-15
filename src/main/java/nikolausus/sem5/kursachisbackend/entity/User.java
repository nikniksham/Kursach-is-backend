package nikolausus.sem5.kursachisbackend.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128, unique = true)
    private String login;

    @Column(nullable = false)
    private byte[] password;

    @ManyToMany
    @JoinTable(
            name = "roles_users",  // Название таблицы связи
            joinColumns = @JoinColumn(name = "users_id"),  // FK на User
            inverseJoinColumns = @JoinColumn(name = "roles_id") // FK на Role
    )
    private Set<Role> roles;
}
