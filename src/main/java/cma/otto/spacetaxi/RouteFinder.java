package cma.otto.spacetaxi;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

/**
 * Service that searches the {@link Highway}s for a {@link Route} from one System to another.
 */
public class RouteFinder {
    private final Map<String, List<Highway>> highwaysByStartSystem;

    public RouteFinder(List<Highway> highways) {
        this.highwaysByStartSystem = highways.stream()
                .collect(Collectors.groupingBy(highway -> highway.start));
    }

    /**
     * Finds the shortest {@link Route} by travel time between two systems.
     *
     * @param start  the start system
     * @param target the target system
     * @return the shortest {@link Route}. May return more than one if travel time is equal.
     */
    public List<Route> findShortestRoute(String start, String target) {
        List<Route> routesWith = findRoutesWith(start, route1 -> !route1.containsLoop(), reached(target));
        Map<Integer, List<Route>> collect = routesWith.stream().collect(groupingBy(Route::calculateTravelTime));
        return collect.getOrDefault(collect.keySet().stream().min(Integer::compareTo).orElse(0), emptyList());
    }

    /**
     * Finds {@link Route}s from start to target with the exact amount of stops including the start system.
     *
     * @param start      the start system
     * @param target     the target system
     * @param exactStops the exact amount of stops
     * @return the {@link Route}s matching the criteria
     */
    public List<Route> findRoutesWithExactStops(String start, String target, int exactStops) {
        return findRoutesWith(start, maxStops(exactStops), reached(target).and(route -> route.getStopsCount(route) == exactStops));
    }

    /**
     * Finds {@link Route}s from start to target with the maximum amount of stops including the start system.
     *
     * @param start    the start system
     * @param target   the target system
     * @param maxStops the maximum amount of stops
     * @return the {@link Route}s matching the criteria
     */
    public List<Route> findRouteWithMaxStops(String start, String target, int maxStops) {
        return findRoutesWith(start, target, maxStops(maxStops));
    }

    /**
     * Finds {@link Route}s from start to target with the maximum amount of travel time.
     *
     * @param start         the start system
     * @param target        the target system
     * @param maxTravelTime the maximum amount of travel time
     * @return the {@link Route}s matching the criteria
     */
    public List<Route> findRoutesWithMaxTravelTime(String start, String target, int maxTravelTime) {
        return findRoutesWith(start, target, route -> route.calculateTravelTime() <= maxTravelTime);
    }

    private List<Route> findRoutesWith(String start, String target, Predicate<Route> routeCondition) {
        return findRoutesWith(start, routeCondition, reached(target));
    }

    private List<Route> findRoutesWith(String start, Predicate<Route> routeCondition, Predicate<Route> addCondition) {
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

    private List<Route> findNewCandidates(Route start) {
        List<Highway> highways = start.getFinalTarget()
                .map(target -> this.highwaysByStartSystem.getOrDefault(target, emptyList()))
                .orElse(emptyList());
        return highways.stream()
                .map(start::extendBy)
                .collect(toCollection(LinkedList::new));
    }

    private Predicate<Route> maxStops(int exactStops) {
        return route -> route.getStopsCount(route) <= exactStops;
    }

    private Predicate<Route> reached(String target) {
        return route -> route.hasReached(target);
    }
}