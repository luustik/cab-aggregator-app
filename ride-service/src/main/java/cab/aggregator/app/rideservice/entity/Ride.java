package cab.aggregator.app.rideservice.entity;

import cab.aggregator.app.rideservice.entity.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ride")
@Getter
@Setter
@NoArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id", nullable = true)
    private Long driverId;

    @Column(name = "passenger_id", nullable = false)
    private Long passengerId;

    @Column(name = "departure_address", nullable = false)
    private String departureAddress;

    @Column(name = "arrival_address", nullable = false)
    private String arrivalAddress;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;

    @Column(name = "cost", precision = 5, scale = 2, nullable = false)
    private BigDecimal cost;
}
