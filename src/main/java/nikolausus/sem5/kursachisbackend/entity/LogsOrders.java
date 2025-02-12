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
@Table(name="logs_orders")
public class LogsOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @ManyToOne
    @JoinColumn(name = "orders_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "status_orders_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StatusOrders statusOrders;

    @Column(nullable = false, updatable = false)
    private Timestamp creationDate;

    @PrePersist
    protected void onCreate() {
        creationDate = Timestamp.from(Instant.now());
    }
}
