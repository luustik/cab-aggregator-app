package cab.aggregator.app.driverservice.repository;

import cab.aggregator.app.driverservice.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    Optional<Car> findById(int id);

    Optional<Car> findByCarNumber(String carNumber);

    Page<Car> findAllByDriverId(int driverId, Pageable pageable);

    boolean existsByCarNumber(String carNumber);
}
