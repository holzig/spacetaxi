package cma.otto.spacetaxi;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * Utility class to parse a String int a {@link Route}. The systems need to be separated by "->".
 */
public class RouteParser {
    private final Map<Object, List<Highway>> highways;

    public RouteParser(Map<Object, List<Highway>> highways) {
        this.highways = highways;
    }

    public RouteParser(List<Highway> highways) {
        this(highways.stream().collect(groupingBy(highway -> highway.start)));
    }

    /**
     * Converts a string into a {@link Route} if possible.
     *
     * @param routeAsString A String with all system separated by "->"
     * @return the {@link Route} object
     * @throws NoSuchRouteException If the {@link Route} is not valid.
     */
    public Route parse(String routeAsString) throws NoSuchRouteException {
        String[] steps = routeAsString.split("\\s*->\\s*");
        if (steps.length < 2) {
            throw new NoSuchRouteException();
        }
        Route route = new Route();
        for (int i = 0; i < steps.length - 1; i++) {
            String start = steps[i];
            String target = steps[i + 1];
            Highway highway = this.highways.getOrDefault(start, Collections.emptyList()).stream()
                    .filter(h -> h.target.equals(target))
                    .findFirst()
                    .orElseThrow(NoSuchRouteException::new);
            route.addHighway(highway);
        }
        return route;
    }
}