package nikolausus.sem5.kursachisbackend.repository;

import nikolausus.sem5.kursachisbackend.entity.StatusApplications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusApplicationsRepository extends JpaRepository<StatusApplications, Long> {
}
