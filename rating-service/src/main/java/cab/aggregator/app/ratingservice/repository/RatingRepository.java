package cab.aggregator.app.ratingservice.repository;

import cab.aggregator.app.ratingservice.entity.Rating;
import cab.aggregator.app.ratingservice.entity.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findAllByUserIdAndUserRole(Long userId, UserRole userRole);

    Page<Rating> findAllByUserIdAndUserRole(Long userId, UserRole userRole, Pageable pageable);

    Page<Rating> findAllByUserRole(UserRole userRole, Pageable pageable);

    Optional<Rating> findByRideIdAndUserRole(Long rideId, UserRole userRole);

    boolean existsByRideIdAndUserRole(Long rideId, UserRole userRole);
}
