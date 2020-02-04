package cma.otto.spacetaxi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Exercises {

    private final RouteParser routeParser = new RouteParser(DefaultDataset.highwaysByStartSystem);
    private final RouteFinder routeFinder = new RouteFinder(DefaultDataset.highways);

    @Test
    @DisplayName("The distance of route Solar System -> Alpha Centauri -> Sirius")
    public void exercise1() {
        assertThat(routeParser.parse("Solar System -> Alpha Centauri -> Sirius").calculateTravelTime()).isEqualTo(9);
    }

    @Test
    @DisplayName("The distance of route Solar System -> Betelgeuse")
    public void exercise2() {
        assertThat(routeParser.parse("Solar System -> Betelgeuse").calculateTravelTime()).isEqualTo(5);
    }

    @Test
    @DisplayName("The distance of route Solar System -> Betelgeuse -> Sirius")
    public void exercise3() {
        assertThat(routeParser.parse("Solar System -> Betelgeuse -> Sirius").calculateTravelTime()).isEqualTo(13);
    }

    @Test
    @DisplayName("The distance of route Solar System -> Vega -> Alpha Centauri -> Sirius -> Betelgeuse")
    public void exercise4() {
        assertThat(routeParser.parse("Solar System -> Vega -> Alpha Centauri -> Sirius -> Betelgeuse").calculateTravelTime()).isEqualTo(22);
    }

    @Test
    @DisplayName("The distance of route Solar System -> Vega -> Betelgeuse")
    public void exercise5() {
        Exception ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> assertThat(routeParser.parse("Solar System -> Vega -> Betelgeuse").calculateTravelTime()).isEqualTo(9)
        );
        assertThat(ex).hasMessage("NO SUCH ROUTE");
    }

    @Test
    @DisplayName("Determine all routes starting at Sirius and ending at Sirius with a maximum of 3 stops")
    public void exercise6() {
        List<Route> routes = routeFinder.findRouteWithMaxStops("Sirius", "Sirius", 3);
        assertThat(routes)
                .containsOnly(
                        routeParser.parse("Sirius -> Betelgeuse -> Sirius"),
                        routeParser.parse("Sirius -> Vega -> Alpha Centauri -> Sirius")
                );
    }

    @Test
    @DisplayName("Determine the number of routes starting at the solar system and ending at Sirius with exactly 3 stops in between.")
    public void exercise7() {
        List<Route> routes = routeFinder.findRoutesWithExactStops("Solar System", "Sirius", 4);
        assertThat(routes)
                .containsExactlyInAnyOrder(
                        routeParser.parse("Solar System -> Alpha Centauri -> Sirius -> Betelgeuse -> Sirius"),
                        routeParser.parse("Solar System -> Betelgeuse -> Sirius -> Betelgeuse -> Sirius"),
                        routeParser.parse("Solar System -> Betelgeuse -> Vega -> Alpha Centauri -> Sirius")
                );
    }


    @Test
    @DisplayName("Determine the duration of the shortest routes (in travel time) between solar system and  Sirius")
    public void exercise8() {
        assertThat(routeFinder.findShortestRoute("Solar System", "Sirius"))
                .extractingResultOf("calculateTravelTime")
                .containsOnly(9);
    }

    @Test
    @DisplayName("Determine the duration of the shortest routes (in travel time) starting at Alpha Centauri and ending at Alpha Centauri")
    public void exercise9() {
        assertThat(routeFinder.findShortestRoute("Alpha Centauri", "Alpha Centauri"))
                .extractingResultOf("calculateTravelTime")
                .containsOnly(9);
    }

    @Test
    @DisplayName("Determine all different routes starting at Sirius and ending at Sirius with an over travel time less than 30.")
    public void exercise10() {
        List<Route> routes = routeFinder.findRoutesWithMaxTravelTime("Sirius", "Sirius", 29);
        assertThat(routes)
                .containsOnly(
                        routeParser.parse("Sirius -> Betelgeuse -> Sirius"),
                        routeParser.parse("Sirius -> Vega -> Alpha Centauri -> Sirius"),
                        routeParser.parse("Sirius -> Vega -> Alpha Centauri -> Sirius -> Betelgeuse -> Sirius"),
                        routeParser.parse("Sirius -> Betelgeuse -> Sirius -> Vega-> Alpha Centauri -> Sirius"),
                        routeParser.parse("Sirius -> Betelgeuse -> Vega-> Alpha Centauri -> Sirius"),
                        routeParser.parse("Sirius -> Vega -> Alpha Centauri -> Sirius -> Vega-> Alpha Centauri -> Sirius"),
                        routeParser.parse("Sirius -> Vega -> Alpha Centauri -> Sirius -> Vega -> Alpha Centauri -> Sirius -> Vega -> Alpha Centauri -> Sirius")
                );
    }
}
