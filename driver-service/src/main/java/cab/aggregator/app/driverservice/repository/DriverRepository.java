package cab.aggregator.app.driverservice.repository;

import cab.aggregator.app.driverservice.entity.Driver;
import cab.aggregator.app.driverservice.entity.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    Optional<Driver> findByIdAndDeletedFalse(int driverId);

    List<Driver> findByDeletedTrue();

    Page<Driver> findAllByGenderAndDeletedFalse(Gender gender, Pageable pageable);

    Page<Driver> findByDeletedFalse(Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String driverPhoneNumber);
}
