package cma.otto.spacetaxi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculateTravelTimeTest {

    @Test
    public void testSolarSystemAlphaCentauriSirius() {
        assertThat(calculateTravelTime("Solar System", "Alpha Centauri", "Sirius")).isEqualTo(9);
    }

    @Test
    public void testSolarSystemBetelgeuse() {
        assertThat(calculateTravelTime("Solar System", "Betelgeuse")).isEqualTo(5);
    }

    @Test
    public void testSolarSystemBetelgeuseSirius() {
        assertThat(calculateTravelTime("Solar System", "Betelgeuse", "Sirius")).isEqualTo(13);
    }

    @Test
    public void testSolarSystemVegaAlphaCentauriSiriusBetelgeuse() {
        assertThat(calculateTravelTime("Solar System", "Vega", "Alpha Centauri", "Sirius", "Betelgeuse")).isEqualTo(22);
    }

    @Test
    public void testSolarSystemVegaBetelgeuse() {
        Exception ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculateTravelTime("Solar System", "Vega", "Betelgeuse");
        });
        assertThat(ex).hasMessage("NO SUCH ROUTE");
    }

    private static class Highway {
        private final String start;
        private final String target;
        private final int travelTime;

        private Highway(String start, String target, int travelTime) {
            this.start = start;
            this.target = target;
            this.travelTime = travelTime;
        }
    }

    private Collection<Highway> highways = Arrays.asList(
            new Highway("Solar System", "Alpha Centauri", 5),
            new Highway("Alpha Centauri", "Sirius", 4),
            new Highway("Sirius", "Betelgeuse", 8),
            new Highway("Betelgeuse", "Sirius", 8),
            new Highway("Betelgeuse", "Vega", 6),
            new Highway("Solar System", "Betelgeuse", 5),
            new Highway("Sirius", "Vega", 2),
            new Highway("Vega", "Alpha Centauri", 3),
            new Highway("Solar System", "Vega", 7)
    );

    private int calculateTravelTime(String... systems) {
        int time = 0;
        for (int i = 0; i < systems.length - 1; i++) {
            String start = systems[i];
            String target = systems[i + 1];
            Highway highway = highways.stream()
                    .filter((h) -> h.start.equals(start) && h.target.equals(target))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("NO SUCH ROUTE"));
            time += highway.travelTime;
        }
        return time;
    }

}
