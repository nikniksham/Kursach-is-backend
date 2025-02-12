package nikolausus.sem5.kursachisbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String target_name;

    @Column(nullable = false)
    private Integer target_isu_num;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @Column(nullable = false, updatable = false)
    private Timestamp publication_date;

    @ManyToOne
    @JoinColumn(name = "status_orders_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private StatusOrders statusOrders;

    @PrePersist
    protected void onCreate() {
        publication_date = Timestamp.from(Instant.now());
    }


}
