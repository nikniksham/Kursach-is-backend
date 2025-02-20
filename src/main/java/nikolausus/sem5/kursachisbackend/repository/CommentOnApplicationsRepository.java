package nikolausus.sem5.kursachisbackend.repository;

import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.entity.CommentOnApplications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentOnApplicationsRepository extends JpaRepository<CommentOnApplications, Long> {
    public Optional<CommentOnApplications> getCommentOnApplicationsByApplications(Applications applications);
    public boolean existsByApplications(Applications applications);
    public Optional<CommentOnApplications> getCommentOnApplicationsById(Long id);
}
