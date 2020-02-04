package cma.otto.spacetaxi;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class FindRoutesTest {

    private final Highway a_b = new Highway("A", "B", 1);
    private final Highway a_c = new Highway("A", "C", 1);
    private final Highway a_d = new Highway("A", "D", 1);
    private final Highway b_a = new Highway("B", "A", 1);
    private final Highway b_c = new Highway("B", "C", 1);
    private final Highway c_a = new Highway("C", "A", 1);
    private final Highway c_b = new Highway("C", "B", 1);


    @Test
    public void testCircle() {
        List<Highway> highways = asList(a_b, b_a);

        List<Route> routesCircle = new RouteFinder(highways).findRouteWithMaxStops("A", "A", 5);
        assertThat(routesCircle).containsOnly(
                new Route(a_b, b_a),
                new Route(a_b, b_a, a_b, b_a)
        );
    }

    @Test
    public void testMultipleStepsMultipleRoutes() {
        List<Highway> highways = asList(a_b, b_c, a_c);
        List<Route> routes = new RouteFinder(highways).findRouteWithMaxStops("A", "C", 2);
        assertThat(routes).containsOnly(new Route(a_b, b_c), new Route(a_c));
    }

    @Test
    public void testMultiplePossibleRoundtrips() {
        List<Highway> highways = asList(a_b, b_a, a_c, c_b);
        List<Route> routes = new RouteFinder(highways).findRouteWithMaxStops("A", "A", 3);
        assertThat(routes).containsOnly(new Route(a_b, b_a), new Route(a_c, c_b, b_a));
    }

    @Test
    public void testWithNoRouteFound() {
        List<Highway> highways = singletonList(a_b);
        assertThat(new RouteFinder(highways).findShortestRoute("A", "C")).isEmpty();
    }

    @Test
    public void testWithNoRouteFoundButCircleAvailable() {
        List<Highway> highways = asList(a_b, b_a);
        assertThat(new RouteFinder(highways).findShortestRoute("A", "C")).isEmpty();
    }

    @Test
    public void testShortestRoute() {
        List<Highway> highways = singletonList(a_b);

        List<Route> route = new RouteFinder(highways).findShortestRoute("A", "B");
        assertThat(route).containsOnly(new Route(a_b));
    }

    @Test
    public void testShortestRouteRoundtripPossibleButNotTraveled() {
        List<Highway> highways = asList(a_b, b_c);

        List<Route> route = new RouteFinder(highways).findShortestRoute("A", "B");
        assertThat(route).containsOnly(new Route(a_b));
    }

    @Test
    public void testShortestRouteRoundtripTraveled() {
        List<Highway> highways = asList(a_b, b_a);

        List<Route> route = new RouteFinder(highways).findShortestRoute("A", "A");
        assertThat(route).containsOnly(new Route(a_b, b_a));
    }

    @Test
    public void testShortestRouteMultipleEqualRoutesPossible() {
        List<Highway> highways = asList(a_b, b_a, a_c, c_a);

        List<Route> route = new RouteFinder(highways).findShortestRoute("A", "A");
        assertThat(route).containsOnly(new Route(a_b, b_a), new Route(a_c, c_a));
    }

    @Test
    public void testShortestRouteRoundtripMultipleDifferentRoutesPossible() {
        List<Highway> highways = asList(a_b, b_a, a_c, h("C", "A", 2));

        List<Route> route = new RouteFinder(highways).findShortestRoute("A", "A");
        assertThat(route).containsOnly(new Route(a_b, b_a));
    }

    @Test
    public void testShortestRouteMultipleRoutesWithFewerSteps() {
        List<Highway> highways = asList(a_b, b_c, h("A", "C", 5));

        List<Route> route = new RouteFinder(highways).findShortestRoute("A", "C");
        assertThat(route).containsOnly(new Route(a_b, b_c));
    }

    @Test
    public void testSinglePossibleRoundtripsWithDeadEnds() {
        List<Highway> highways = asList(a_b, a_c, a_d, c_a);
        List<Route> routes = new RouteFinder(highways).findShortestRoute("A", "A");
        assertThat(routes).containsOnly(new Route(a_c, c_a));
    }

    @Test
    public void testShortestRouteMultipleDifferentRoutesPossible() {
        List<Highway> highways = asList(
                a_b,
                h("B", "D", 2),
                a_c,
                h("C", "D", 1)
        );

        List<Route> routes = new RouteFinder(highways).findShortestRoute("A", "D");
        assertThat(routes).containsOnly(new Route(a_c, h("C", "D", 1)));
    }

    @Test
    public void testShortestRouteMultipleLongerRoutes() {
        List<Highway> highways = asList(
                h("A", "B", 1),
                h("B", "C", 1),
                h("C", "F", 3),
                h("A", "D", 1),
                h("D", "E", 2),
                h("E", "F", 1)
        );

        List<Route> route = new RouteFinder(highways).findShortestRoute("A", "F");
        assertThat(route).containsOnly(new Route(h("A", "D", 1), h("D", "E", 2), h("E", "F", 1)));
    }

    @Test
    public void testShortestRouteMultipleLongerRoutesWithPossibleWayBack() {
        List<Highway> highways = asList(
                h("A", "B", 1),
                h("B", "A", 1),
                h("B", "C", 1),
                h("C", "F", 3),
                h("A", "D", 1),
                h("D", "A", 1),
                h("D", "E", 2),
                h("E", "F", 1)
        );

        List<Route> route = new RouteFinder(highways).findShortestRoute("A", "F");
        assertThat(route).containsOnly(new Route(h("A", "D", 1), h("D", "E", 2), h("E", "F", 1)));
    }

    @Test
    public void testFindRouteWithMaxStops() {
        List<Highway> highways = asList(
                a_b,
                b_c,
                h("A", "C", 4)
        );

        List<Route> route = new RouteFinder(highways).findRouteWithMaxStops("A", "C", 1);
        assertThat(route).containsOnly(new Route(h("A", "C", 4)));
    }

    @Test
    public void testFindRouteWithMaxStops2() {
        List<Highway> highways = asList(
                a_b,
                b_c,
                h("A", "C", 4)
        );

        List<Route> route = new RouteFinder(highways).findRouteWithMaxStops("A", "C", 2);
        assertThat(route).containsExactlyInAnyOrder(
                new Route(h("A", "C", 4)),
                new Route(a_b, b_c)
        );
    }

    @Test
    public void testFindRouteWithMaxStops4() {
        List<Highway> highways = asList(
                a_b,
                b_a,
                b_c,
                a_c
        );
        RouteParser routeParser = new RouteParser(highways);

        List<Route> route = new RouteFinder(highways).findRouteWithMaxStops("A", "C", 4);
        assertThat(route).containsExactlyInAnyOrder(
                routeParser.parse("A -> C"),
                routeParser.parse("A -> B -> C"),
                routeParser.parse("A -> B -> A -> B -> C"),
                routeParser.parse("A -> B -> A -> C")
        );
    }

    private Highway h(String s, String t, int time) {
        return new Highway(s, t, time);
    }
}
