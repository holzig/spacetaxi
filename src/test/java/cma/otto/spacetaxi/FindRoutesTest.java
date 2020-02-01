package cma.otto.spacetaxi;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void testRouteConditionCheckFailed() {
        List<Highway> highways = Arrays.asList(a_b, a_c, b_c, c_a);
        RouteCondition check = mock(RouteCondition.class);
        when(check.test(ArgumentMatchers.any(Route.class))).thenReturn(false);

        assertThat(findRoutes(highways, "B", "A", Collections.singletonList(check))).isEmpty();
    }

    @Test
    public void testRouteConditionCheckSuccess() {
        List<Highway> highways = Arrays.asList(a_b, a_c, b_c, c_a);
        RouteCondition check = mock(RouteCondition.class);
        when(check.test(ArgumentMatchers.any(Route.class))).thenReturn(true);

        assertThat(findRoutes(highways, "B", "A", Collections.singletonList(check))).hasSize(1);
    }

    @Test
    public void testMultipleRouteConditionsOneFails() {
        List<Highway> highways = Arrays.asList(a_b, a_c, b_c, c_a);
        RouteCondition success = mock(RouteCondition.class);
        when(success.test(ArgumentMatchers.any(Route.class))).thenReturn(true);
        RouteCondition failure = mock(RouteCondition.class);
        when(failure.test(ArgumentMatchers.any(Route.class))).thenReturn(false);

        assertThat(findRoutes(highways, "B", "A", Arrays.asList(success, failure))).isEmpty();
    }

    private List<Route> findRoutes(List<Highway> highways, String start, String target) {
        return findRoutes(highways, start, target, Collections.emptyList());
    }

    private List<Route> findRoutes(List<Highway> highways, String start, String target, List<Predicate<Route>> checks) {
        Map<String, List<Highway>> highwaysByStartSystem = highways.stream()
                .collect(Collectors.groupingBy(highway -> highway.start));
        Predicate<Route> filter = checks.stream()
                .reduce(x -> true, Predicate::and);
        return findRoutes(highwaysByStartSystem, start, target).stream()
                .filter(filter)
                .collect(toList());
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
