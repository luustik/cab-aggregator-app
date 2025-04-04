package cab.aggregator.app.rideservice.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Service
public class CalculationCost {

    private static final int PRECISION = 5;
    private static final int SCALE = 2;
    private static final Random RANDOM = new Random();

    public BigDecimal generatePrice() {
        int integerPart = RANDOM.nextInt((int) Math.pow(10, PRECISION - SCALE));
        int fractionalPart = RANDOM.nextInt((int) Math.pow(10, SCALE));
        BigDecimal price = new BigDecimal(integerPart + "." + String.format("%0" + SCALE + "d", fractionalPart));
        return price.setScale(SCALE, RoundingMode.HALF_UP);
    }

}
