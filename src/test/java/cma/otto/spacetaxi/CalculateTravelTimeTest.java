package cma.otto.spacetaxi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

    private int calculateTravelTime(String... systems) {
        int time = 0;
        for (int i = 0; i < systems.length - 1; i++) {
            String start = systems[i];
            String target = systems[i + 1];
            switch (start) {
                case "Solar System":
                    switch (target) {
                        case "Alpha Centauri":
                            time += 5;
                            break;
                        case "Betelgeuse":
                            time += 5;
                            break;
                        case "Vega":
                            time += 7;
                            break;
                        default:
                            throw new IllegalArgumentException("NO SUCH ROUTE");
                    }
                    break;
                case "Alpha Centauri":
                    switch (target) {
                        case "Sirius":
                            time += 4;
                            break;
                        default:
                            throw new IllegalArgumentException("NO SUCH ROUTE");
                    }
                    break;
                case "Sirius":
                    switch (target) {
                        case "Betelgeuse":
                            time += 8;
                            break;
                        case "Vega":
                            time += 2;
                            break;
                        default:
                            throw new IllegalArgumentException("NO SUCH ROUTE");
                    }
                    break;
                case "Betelgeuse":
                    switch (target) {
                        case "Sirius":
                            time += 8;
                            break;
                        case "Vega":
                            time += 6;
                            break;
                        default:
                            throw new IllegalArgumentException("NO SUCH ROUTE");
                    }
                    break;
                case "Vega":
                    switch (target) {
                        case "Alpha Centauri":
                            time += 3;
                            break;
                        default:
                            throw new IllegalArgumentException("NO SUCH ROUTE");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("NO SUCH ROUTE");
            }
        }
        return time;
    }

}
