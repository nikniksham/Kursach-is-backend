package nikolausus.sem5.kursachisbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    @JsonIgnore  // Теперь Role не содержит User, но User содержит Role
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
