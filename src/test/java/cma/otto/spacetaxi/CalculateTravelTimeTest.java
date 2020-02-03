package cma.otto.spacetaxi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculateTravelTimeTest {

    private final DefaultDataset defaultDataset = new DefaultDataset();

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

    private int calculateTravelTime(String... systems) {
        Route route = new Route();
        for (int i = 0; i < systems.length - 1; i++) {
            String start = systems[i];
            String target = systems[i + 1];
            Highway highway = DefaultDataset.highways.stream()
                    .filter((h) -> h.start.equals(start) && h.target.equals(target))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("NO SUCH ROUTE"));
            route.addHighway(highway);
        }
        return route.calculateTravelTime();
    }

}
