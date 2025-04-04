package cab.aggregator.app.rideservice.repository;

import cab.aggregator.app.rideservice.entity.Ride;
import cab.aggregator.app.rideservice.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    Page<Ride> findAllByStatus(Status status, Pageable pageable);

    Page<Ride> findAllByDriverId(Long driverId, Pageable pageable);

    Page<Ride> findAllByPassengerId(Long passengerId, Pageable pageable);

    Page<Ride> findAllByOrderDateTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
