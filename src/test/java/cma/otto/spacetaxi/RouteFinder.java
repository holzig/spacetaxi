package cma.otto.spacetaxi;

import java.util.*;
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

    public List<Route> findRoutesCircle(String start, String target, List<Predicate<Route>> checks) {
        return findRoutesc(start, checks, null, route -> route.usedHighways.get(route.usedHighways.size() - 1).target.equals(target), emptyList());
    }


    private List<Route> findRoutesc(String start, List<Predicate<Route>> checks, Route prevroute, Predicate<Route> endCondition, List<Route> foundRoutes) {
        List<Highway> highwaysFromHere = this.highwaysByStartSystem.get(start);
        if (highwaysFromHere == null) {
            return foundRoutes;
        }
        return highwaysFromHere.stream()
                .map(highway -> {
                    Route route = new Route();
                    if (prevroute != null) {
                        prevroute.usedHighways.forEach(route::addHighway);
                    }
                    route.addHighway(highway);

                    boolean matches = checks.stream().allMatch(c -> c.test(route));
                    List<Route> routes = new ArrayList<>(foundRoutes);
                    if (matches) {
                        if (endCondition.test(route)) {
                            routes.add(route);
                        }
                        return findRoutesc(highway.target, checks, route, endCondition, routes);
                    } else {
                        return routes;
                    }
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Route> findShortestRoute(String start, String target) {
        List<Highway> highways = this.highwaysByStartSystem.get(start);
        Queue<Route> candidateRoutes = highways.stream()
                .map(Route::new)
                .collect(Collectors.toCollection(LinkedList::new));

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

    public List<Route> findNewCandidates(Route start) {
        List<Highway> highways = this.highwaysByStartSystem.getOrDefault(start.getFinalTarget(), emptyList());
        return highways.stream()
                .map(start::extendBy)
                .collect(Collectors.toCollection(LinkedList::new));
    }

}