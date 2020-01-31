package cma.otto.spacetaxi;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class FindRoutesTest {

    private final Highway a_b = new Highway("A", "B", 1);
    private final Highway a_c = new Highway("A", "C", 2);
    private final Highway a_d = new Highway("A", "D", 1);
    private final Highway b_a = new Highway("B", "A", 2);
    private final Highway b_c = new Highway("B", "C", 2);
    private final Highway c_a = new Highway("C", "A", 2);
    private final Highway c_b = new Highway("C", "B", 2);

    @Test
    public void testWithSingleHighway() {
        List<Highway> highways = Arrays.asList(a_b);
        assertThat(findRoutes(highways, "A", "B"))
                .containsOnly(new Route(a_b));
    }

    @Test
    public void testWithNoRouteFound() {
        List<Highway> highways = Arrays.asList(a_b);
        assertThat(findRoutes(highways, "A", "C")).isEmpty();
    }

    @Test
    public void testMultipleStepsSingleRoute() {
        List<Highway> highways = Arrays.asList(a_b, b_c);
        List<Route> routes = findRoutes(highways, "A", "C");
        assertThat(routes).containsOnly(new Route(a_b, b_c));
    }

    @Test
    public void testMultipleStepsMultipleRoutes() {
        List<Highway> highways = Arrays.asList(a_b, b_c, a_c);
        List<Route> routes = findRoutes(highways, "A", "C");
        assertThat(routes).containsOnly(new Route(a_b, b_c), new Route(a_c));
    }

    @Test
    public void testRoundtrip() {
        List<Highway> highways = Arrays.asList(a_b, b_a);
        List<Route> routes = findRoutes(highways, "A", "A");
        assertThat(routes).containsOnly(new Route(a_b, b_a));
    }

    @Test
    public void testMultiplePossibleRoundtrips() {
        List<Highway> highways = Arrays.asList(a_b, b_a, a_c, c_b);
        List<Route> routes = findRoutes(highways, "A", "A");
        assertThat(routes).containsOnly(new Route(a_b, b_a), new Route(a_c, c_b, b_a));
    }

    @Test
    public void testSinglePossibleRoundtripsWithDeadEnds() {
        List<Highway> highways = Arrays.asList(a_b, a_c, a_d, c_a);
        List<Route> routes = findRoutes(highways, "A", "A");
        assertThat(routes).containsOnly(new Route(a_c, c_a));
    }

    private List<Route> findRoutes(List<Highway> highways, String start, String target) {
        Map<String, List<Highway>> highwaysByStartSystem = highways.stream().collect(Collectors.groupingBy(highway -> highway.start));
        return findRoutes(highwaysByStartSystem, start, target);
    }

    private List<Route> findRoutes(Map<String, List<Highway>> highwaysByStartSystem, String start, String target) {
        List<Highway> highwaysFromHere = highwaysByStartSystem.getOrDefault(start, Collections.emptyList());
        return highwaysFromHere.stream()
                .map(highway -> {
                    if (isDestination(target, highway)) {
                        return Collections.singletonList(new Route(highway));
                    } else {
                        return findRoutes(highwaysByStartSystem, highway.target, target).stream()
                                .map(route -> new Route(highway, route.usedHighways))
                                .collect(toList());
                    }
                }).flatMap(List::stream)
                .collect(toList());
    }

    private boolean isDestination(String target, Highway highway) {
        return highway.target.equals(target);
    }

}
