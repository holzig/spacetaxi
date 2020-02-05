package cma.otto.spacetaxi;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

public class RouteFinder {
    private final Map<String, List<Highway>> highwaysByStartSystem;

    public RouteFinder(List<Highway> highways) {
        this.highwaysByStartSystem = highways.stream()
                .collect(Collectors.groupingBy(highway -> highway.start));
    }

    public List<Route> findShortestRoute(String start, String target) {
        List<Route> routesWith = findRoutesWith(start, route1 -> !route1.containsLoop(), reached(target));
        Map<Integer, List<Route>> collect = routesWith.stream().collect(groupingBy(Route::calculateTravelTime));
        return collect.getOrDefault(collect.keySet().stream().min(Integer::compareTo).orElse(0), emptyList());
    }

    public List<Route> findRoutesWithExactStops(String start, String target, int exactStops) {
        return findRoutesWith(start, maxStops(exactStops), reached(target).and(route -> route.usedHighways.size() == exactStops));
    }

    public List<Route> findRouteWithMaxStops(String start, String target, int maxStops) {
        return findRoutesWith(start, target, maxStops(maxStops));
    }

    public List<Route> findRoutesWithMaxTravelTime(String start, String target, int maxTravelTime) {
        return findRoutesWith(start, target, route -> route.calculateTravelTime() <= maxTravelTime);
    }

    public List<Route> findRoutesWith(String start, String target, Predicate<Route> routeCondition) {
        return findRoutesWith(start, routeCondition, reached(target));
    }

    public List<Route> findRoutesWith(String start, Predicate<Route> routeCondition, Predicate<Route> addCondition) {
        Queue<Route> candidateRoutes = findInitialCandidates(start);

        List<Route> routes = new ArrayList<>();
        while (!candidateRoutes.isEmpty()) {
            Route candidate = candidateRoutes.poll();
            if (routeCondition.test(candidate)) {
                if (addCondition.test(candidate)) {
                    routes.add(candidate);
                }
                candidateRoutes.addAll(findNewCandidates(candidate));
            }
        }
        return routes;
    }

    private Queue<Route> findInitialCandidates(String start) {
        List<Highway> highways = this.highwaysByStartSystem.get(start);
        return highways.stream()
                .map(Route::new)
                .collect(toCollection(LinkedList::new));
    }

    public List<Route> findNewCandidates(Route start) {
        List<Highway> highways = this.highwaysByStartSystem.getOrDefault(start.getFinalTarget(), emptyList());
        return highways.stream()
                .map(start::extendBy)
                .collect(toCollection(LinkedList::new));
    }

    private Predicate<Route> maxStops(int exactStops) {
        return route -> route.usedHighways.size() <= exactStops;
    }

    private Predicate<Route> reached(String target) {
        return route -> route.hasReached(target);
    }
}