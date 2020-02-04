package cma.otto.spacetaxi;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class RouteFinder {
    private Map<String, List<Highway>> highwaysByStartSystem;

    public RouteFinder(List<Highway> highways) {
        this.highwaysByStartSystem = highways.stream()
                .collect(Collectors.groupingBy(highway -> highway.start));
    }

    List<Route> findRoutes(String start, String target) {
        return findRoutes(start, target, emptyList());
    }

    List<Route> findRoutes(String start, String target, List<Predicate<Route>> checks) {
        Predicate<Route> filter = checks.stream()
                .reduce(x -> true, Predicate::and);
        return findRoutesi(start, target).stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    private List<Route> findRoutesi(String start, String target) {
        List<Highway> highwaysFromHere = this.highwaysByStartSystem.getOrDefault(start, emptyList());
        return highwaysFromHere.stream()
                .map(highway -> {
                    if (isDestination(target, highway)) {
                        return Collections.singletonList(new Route(highway));
                    } else {
                        return findRoutesi(highway.target, target).stream()
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