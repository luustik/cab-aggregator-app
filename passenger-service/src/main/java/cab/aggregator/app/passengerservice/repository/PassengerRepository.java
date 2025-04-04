package cab.aggregator.app.passengerservice.repository;

import cab.aggregator.app.passengerservice.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByIdAndDeletedFalse(int passengerId);

    Optional<Passenger> findByEmailAndDeletedFalse(String email);

    Optional<Passenger> findByPhoneAndDeletedFalse(String phone);

    Page<Passenger> findByDeletedFalse(Pageable pageable);

    Optional<Passenger> findByNameAndEmailAndPhoneAndDeletedIsTrue(String name, String email, String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
