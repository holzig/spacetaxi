package cma.otto.spacetaxi;

import java.util.*;

class Route {
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
}
