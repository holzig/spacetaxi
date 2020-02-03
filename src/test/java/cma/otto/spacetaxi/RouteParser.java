package cma.otto.spacetaxi;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RouteParser {
    private Map<Object, List<Highway>> highways;

    public RouteParser(Map<Object, List<Highway>> highways) {
        this.highways = highways;
    }

    Route parse(String routeAsString) {
        String[] steps = routeAsString.split("\\s*->\\s*");
        if (steps.length < 2) {
            throw new IllegalArgumentException("NO SUCH ROUTE");
        }
        Route route = new Route();
        for (int i = 0; i < steps.length - 1; i++) {
            String start = steps[i];
            String target = steps[i + 1];
            Highway highway = this.highways.getOrDefault(start, Collections.emptyList()).stream()
                    .filter(h -> h.target.equals(target))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("NO SUCH ROUTE"));
            route.addHighway(highway);
        }
        return route;
    }
}