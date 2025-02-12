package nikolausus.sem5.kursachisbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name="comment_on_applications")
public class CommentOnApplications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String text;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @OneToOne
    @JoinColumn(name = "applications_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Applications applications;
}
