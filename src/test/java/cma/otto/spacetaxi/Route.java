package cma.otto.spacetaxi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Route {
    final List<Highway> usedHighways;

    Route(List<Highway> steps) {
        usedHighways = new ArrayList<>();
        usedHighways.addAll(steps);
    }

    Route(Highway... highways) {
        this(Arrays.asList(highways));
    }

    Route(Highway firstStep, List<Highway> remainingSteps) {
        this(firstStep);
        remainingSteps.forEach(this::addHighway);
    }

    public void addHighway(Highway highway) {
        usedHighways.add(highway);
    }

    public int calculateTravelTime() {
        return usedHighways.stream().map(highway -> highway.travelTime).reduce(0, Integer::sum);
    }

    public String getFinalTarget() {
        return usedHighways.get(usedHighways.size() - 1).target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(usedHighways, route.usedHighways);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usedHighways);
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
        return String.format("Route{%s steps: %s}(%s)", steps.size(), String.join(" -> ", steps), calculateTravelTime());
    }

    public Route extendBy(Highway extension) {
        Route route = new Route(usedHighways);
        route.addHighway(extension);
        return route;
    }

    boolean hasReached(String target) {
        return getFinalTarget().equals(target);
    }
}
