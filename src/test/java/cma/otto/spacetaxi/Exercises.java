package cma.otto.spacetaxi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

//TODO: Tests still missing/failing because of missing implementation
public class Exercises {

    private final RouteParser routeParser = new RouteParser(DefaultDataset.highwaysByStartsystem);
    private final RouteFinder routeFinder = new RouteFinder();

    @Test
    @DisplayName("The distance of route Solar System -> Alpha Centauri -> Sirius")
    public void excercise1() {
        assertThat(routeParser.parse("Solar System -> Alpha Centauri -> Sirius").calculateTravelTime()).isEqualTo(9);
    }

    @Test
    @DisplayName("The distance of route Solar System -> Betelgeuse")
    public void excercise2() {
        assertThat(routeParser.parse("Solar System -> Betelgeuse").calculateTravelTime()).isEqualTo(5);
    }

    @Test
    @DisplayName("The distance of route Solar System -> Betelgeuse -> Sirius")
    public void excercise3() {
        assertThat(routeParser.parse("Solar System -> Betelgeuse -> Sirius").calculateTravelTime()).isEqualTo(13);
    }

    @Test
    @DisplayName("The distance of route Solar System -> Vega -> Alpha Centauri -> Sirius -> Betelgeuse")
    public void excercise4() {
        assertThat(routeParser.parse("Solar System -> Vega -> Alpha Centauri -> Sirius -> Betelgeuse").calculateTravelTime()).isEqualTo(22);
    }

    @Test
    @DisplayName("The distance of route Solar System -> Vega -> Betelgeuse")
    public void excercise5() {
        Exception ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            assertThat(routeParser.parse("Solar System -> Vega -> Betelgeuse").calculateTravelTime()).isEqualTo(9);
        });
        assertThat(ex).hasMessage("NO SUCH ROUTE");
    }

    @Test
    @DisplayName("Determine all routes starting at Sirius and ending at Sirius with a maximum of 3 stops")
    public void excercise6() {
        List<Route> routes = routeFinder.findRoutes(DefaultDataset.highways, "Sirius", "Sirius", singletonList(((route) -> route.usedHighways.size() <= 3)));
        assertThat(routes)
                .containsOnly(
                        routeParser.parse("Sirius -> Betelgeuse -> Sirius"),
                        routeParser.parse("Sirius -> Vega -> Alpha Centauri -> Sirius")
                );
    }

    //@Test
    @DisplayName("Determine the number of routes starting at the solar system and ending at Sirius with exactly 3 stops inbetween.")
    public void excercise7() {
        List<Route> routes = routeFinder.findRoutes(DefaultDataset.highways, "Solar System", "Sirius", singletonList(((route) -> route.usedHighways.size() == 4)));
        assertThat(routes)
                .containsOnly(
                        routeParser.parse("Solar System -> Alpha Centauri -> Sirius -> Betelgeuse -> Sirius"),
                        routeParser.parse("Solar System -> Betelgeuse -> Sirius -> Betelgeuse -> Sirius"),
                        routeParser.parse("Solar System -> Betelgeuse -> Vega -> Alpha Centauri -> Sirius")
                );
    }


    //@Test
    @DisplayName("Determine the duration of the shortest routes (in traveltime) between solar system and  Sirius")
    public void excercise8() {
        //TODO implement
    }

    //@Test
    @DisplayName("Determine the duration of the shortest routes (in traveltime) starting at Alpha Centauri and ending at Alpha Centauri")
    public void excercise9() {
        //TODO implement
    }

    //@Test
    @DisplayName("Determine all different routes starting at Sirius and ending at Sirius with an over traveltime less than 30.")
    public void excercise10() {
        List<Route> routes = routeFinder.findRoutes(DefaultDataset.highways, "Sirius", "Sirius", singletonList(((route) -> route.calculateTravelTime() < 30)));
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
