package cab.aggregator.app.rideservice.service;

import cab.aggregator.app.rideservice.service.impl.CalculationCost;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculationCostTest {

    private final CalculationCost calculationCost = new CalculationCost();
    private static final int PRECISION = 5;
    private static final int SCALE = 2;
    private static final int CYCLES = 1000;

    @Test
    void generatePrice_testRandomness() {
        for (int i = 0; i < CYCLES; i++) {
            BigDecimal cost1 = calculationCost.generatePrice();
            BigDecimal cost2 = calculationCost.generatePrice();

            assertNotEquals(cost1, cost2);
        }
    }

    @Test
    void generatePrice_testLimits() {

        for (int i = 0; i < CYCLES; i++) {
            BigDecimal cost = calculationCost.generatePrice();
            assertEquals(SCALE, cost.scale());
            assertTrue(cost.precision() <= PRECISION);
            assertTrue(cost.compareTo(BigDecimal.ZERO) >= 0);
        }
    }
}
