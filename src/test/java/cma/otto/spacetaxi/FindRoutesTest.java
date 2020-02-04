package cma.otto.spacetaxi;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        assertThat(new RouteFinder(highways).findRoutes("A", "B"))
                .containsOnly(new Route(a_b));
    }

    @Test
    public void testWithNoRouteFound() {
        List<Highway> highways = Arrays.asList(a_b);
        assertThat(new RouteFinder(highways).findRoutes("A", "C")).isEmpty();
    }

    @Test
    public void testMultipleStepsSingleRoute() {
        List<Highway> highways = Arrays.asList(a_b, b_c);
        List<Route> routes = new RouteFinder(highways).findRoutes("A", "C");
        assertThat(routes).containsOnly(new Route(a_b, b_c));
    }

    @Test
    public void testMultipleStepsMultipleRoutes() {
        List<Highway> highways = Arrays.asList(a_b, b_c, a_c);
        List<Route> routes = new RouteFinder(highways).findRoutes("A", "C");
        assertThat(routes).containsOnly(new Route(a_b, b_c), new Route(a_c));
    }

    @Test
    public void testRoundtrip() {
        List<Highway> highways = Arrays.asList(a_b, b_a);
        List<Route> routes = new RouteFinder(highways).findRoutes("A", "A");
        assertThat(routes).containsOnly(new Route(a_b, b_a));
    }

    @Test
    public void testMultiplePossibleRoundtrips() {
        List<Highway> highways = Arrays.asList(a_b, b_a, a_c, c_b);
        List<Route> routes = new RouteFinder(highways).findRoutes("A", "A");
        assertThat(routes).containsOnly(new Route(a_b, b_a), new Route(a_c, c_b, b_a));
    }

    @Test
    public void testSinglePossibleRoundtripsWithDeadEnds() {
        List<Highway> highways = Arrays.asList(a_b, a_c, a_d, c_a);
        List<Route> routes = new RouteFinder(highways).findRoutes("A", "A");
        assertThat(routes).containsOnly(new Route(a_c, c_a));
    }

    @Test
    public void testRouteConditionCheckFailed() {
        List<Highway> highways = Arrays.asList(a_b, a_c, b_c, c_a);
        RouteCondition check = mock(RouteCondition.class);
        when(check.test(ArgumentMatchers.any(Route.class))).thenReturn(false);

        assertThat(new RouteFinder(highways).findRoutes("B", "A", Collections.singletonList(check))).isEmpty();
    }

    @Test
    public void testRouteConditionCheckSuccess() {
        List<Highway> highways = Arrays.asList(a_b, a_c, b_c, c_a);
        RouteCondition check = mock(RouteCondition.class);
        when(check.test(ArgumentMatchers.any(Route.class))).thenReturn(true);

        assertThat(new RouteFinder(highways).findRoutes("B", "A", Collections.singletonList(check))).hasSize(1);
    }

    @Test
    public void testMultipleRouteConditionsOneFails() {
        List<Highway> highways = Arrays.asList(a_b, a_c, b_c, c_a);
        RouteCondition success = mock(RouteCondition.class);
        when(success.test(ArgumentMatchers.any(Route.class))).thenReturn(true);
        RouteCondition failure = mock(RouteCondition.class);
        when(failure.test(ArgumentMatchers.any(Route.class))).thenReturn(false);

        assertThat(new RouteFinder(highways).findRoutes("B", "A", Arrays.asList(success, failure))).isEmpty();
    }

}
