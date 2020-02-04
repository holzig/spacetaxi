package cma.otto.spacetaxi;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class RouteFinder {
    private Map<String, List<Highway>> highwaysByStartSystem;

    public RouteFinder(List<Highway> highways) {
        this.highwaysByStartSystem = highways.stream()
                .collect(Collectors.groupingBy(highway -> highway.start));
    }

    public List<Route> findShortestRoute(String start, String target) {
        Queue<Route> candidateRoutes = findInitialCandidates(start);

        List<Route> rejectedCandidates = new ArrayList<>();

        List<Route> routes = new ArrayList<>();
        while (!candidateRoutes.isEmpty()) {
            Route candidate = candidateRoutes.poll();
            int candidateLength = candidate.calculateTravelTime();
            int lengthOfCurrentRoutes = routes.stream()
                    .map(Route::calculateTravelTime)
                    .findFirst()
                    .orElse(Integer.MAX_VALUE);

            if (candidateLength <= lengthOfCurrentRoutes) {
                if (candidate.hasReached(target)) {
                    if (candidateLength < lengthOfCurrentRoutes) {
                        routes = new ArrayList<>();
                    }
                    routes.add(candidate);
                } else {
                    rejectedCandidates.add(candidate);
                    findNewCandidates(candidate).stream()
                            .filter(route -> rejectedCandidates.stream().noneMatch(route::endsWith))
                            .forEach(candidateRoutes::add);
                }
            } else {
                rejectedCandidates.add(candidate);
            }
        }
        return routes;
    }

    public List<Route> findRoutesWithExactStops(String start, String target, int exactStops) {
        return findRoutesWith(start, maxStops(exactStops), (route, r) -> route.usedHighways.size() == exactStops && route.hasReached(target));
    }

    public List<Route> findRouteWithMaxStops(String start, String target, int maxStops) {
        return findRoutesWith(start, target, maxStops(maxStops));
    }

    public List<Route> findRoutesWithMaxTravelTime(String start, String target, int maxTravelTime) {
        return findRoutesWith(start, target, (route, r) -> route.calculateTravelTime() <= maxTravelTime);
    }

    public List<Route> findRoutesWith(String start, String target, RouteCondition routeCondition) {
        return findRoutesWith(start, routeCondition, (route, r) -> route.hasReached(target));
    }

    public List<Route> findRoutesWith(String start, RouteCondition routeCondition, RouteCondition addCondition) {
        Queue<Route> candidateRoutes = findInitialCandidates(start);

        List<Route> routes = new ArrayList<>();
        while (!candidateRoutes.isEmpty()) {
            Route candidate = candidateRoutes.poll();

            if (routeCondition.check(candidate, routes)) {
                if (addCondition.check(candidate, routes)) {
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
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<Route> findNewCandidates(Route start) {
        List<Highway> highways = this.highwaysByStartSystem.getOrDefault(start.getFinalTarget(), emptyList());
        return highways.stream()
                .map(start::extendBy)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private RouteCondition maxStops(int exactStops) {
        return (route, r) -> route.usedHighways.size() <= exactStops;
    }
}