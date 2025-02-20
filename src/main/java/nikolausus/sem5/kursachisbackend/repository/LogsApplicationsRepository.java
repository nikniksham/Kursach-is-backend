package nikolausus.sem5.kursachisbackend.repository;

import jakarta.transaction.Transactional;
import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.entity.LogsApplications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsApplicationsRepository extends JpaRepository<LogsApplications, Long> {
    @Transactional
    @Query(value = "SELECT log_applications(:whoId, :appId)", nativeQuery = true)
    void createLog(@Param("whoId") long whoId, @Param("appId") long appId);

    public List<LogsApplications> getLogsApplicationsByApplications(Applications applications);
}
