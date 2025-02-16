package nikolausus.sem5.kursachisbackend.repository;

import jakarta.transaction.Transactional;
import nikolausus.sem5.kursachisbackend.entity.LogsApplications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsApplicationsRepository extends JpaRepository<LogsApplications, Long> {
    @Transactional
    @Query(value = "SELECT log_applications(:whoId, :appId)", nativeQuery = true)
    void createLog(@Param("whoId") Long whoId, @Param("appId") Long appId);
}
