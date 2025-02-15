package nikolausus.sem5.kursachisbackend.repository;

import nikolausus.sem5.kursachisbackend.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {

}
