package cma.otto.spacetaxi;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents the {@link Route} consisting of {@link Highway}s a space ship could take.
 */
public class Route {
    private final List<Highway> usedHighways;

    public Route(List<Highway> steps) {
        usedHighways = new ArrayList<>();
        usedHighways.addAll(steps);
    }

    Route(Highway... highways) {
        this(Arrays.asList(highways));
    }

    public void addHighway(Highway highway) {
        usedHighways.add(highway);
    }

    public int calculateTravelTime() {
        return usedHighways.stream().map(highway -> highway.travelTime).reduce(0, Integer::sum);
    }

    public int getStopsCount(Route route) {
        return route.usedHighways.size();
    }

    /**
     * Creates a new {@link Route} which uses the same highways plus the given {@link Highway}
     *
     * @param extension An additional {@link Highway}
     * @return the new {@link Route}
     */
    public Route extendBy(Highway extension) {
        Route route = new Route(usedHighways);
        route.addHighway(extension);
        return route;
    }

    /**
     * Detects if the last target matches the given target.
     *
     * @param target the target to match against
     */
    public boolean hasReached(String target) {
        return getFinalTarget().map(s -> s.equals(target)).orElse(false);
    }

    public Optional<String> getFinalTarget() {
        if (usedHighways.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(usedHighways.get(usedHighways.size() - 1).target);
    }

    /**
     * Detects if the Route travels the same {@link Highway} twice.
     */
    public boolean containsLoop() {
        Map<Highway, Long> counts = usedHighways.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        return counts.values().stream()
                .max(Long::compareTo)
                .orElse(0L) > 1;
    }

    @Override
    public String toString() {
        List<String> steps = new ArrayList<>(usedHighways.size());
        for (int i = 0; i < usedHighways.size(); i++) {
            Highway highway = usedHighways.get(i);
            steps.add(highway.start);
            if (i == usedHighways.size() - 1) {
                steps.add(highway.target);
            }
        }
        return String.format("Route{%s steps: %s}(travel time: %s)", steps.size(), String.join(" -> ", steps), calculateTravelTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(usedHighways);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(usedHighways, route.usedHighways);
    }

}
