package cma.otto.spacetaxi;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RouteFinder {
    public RouteFinder() {
    }

    List<Route> findRoutes(List<Highway> highways, String start, String target) {
        return findRoutes(highways, start, target, Collections.emptyList());
    }

    List<Route> findRoutes(List<Highway> highways, String start, String target, List<Predicate<Route>> checks) {
        Map<String, List<Highway>> highwaysByStartSystem = highways.stream()
                .collect(Collectors.groupingBy(highway -> highway.start));
        Predicate<Route> filter = checks.stream()
                .reduce(x -> true, Predicate::and);
        return findRoutes(highwaysByStartSystem, start, target).stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    List<Route> findRoutes(Map<String, List<Highway>> highwaysByStartSystem, String start, String target) {
        List<Highway> highwaysFromHere = highwaysByStartSystem.getOrDefault(start, Collections.emptyList());
        return highwaysFromHere.stream()
                .map(highway -> {
                    if (isDestination(target, highway)) {
                        return Collections.singletonList(new Route(highway));
                    } else {
                        return findRoutes(highwaysByStartSystem, highway.target, target).stream()
                                .map(route -> new Route(highway, route.usedHighways))
                                .collect(Collectors.toList());
                    }
                }).flatMap(List::stream)
                .collect(Collectors.toList());
    }

    boolean isDestination(String target, Highway highway) {
        return highway.target.equals(target);
    }
}