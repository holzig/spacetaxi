package cma.otto.spacetaxi;

import java.util.ArrayList;
import java.util.List;

class Route {
    private final List<Highway> usedHighways = new ArrayList<>();

    public void addHighway(Highway highway) {
        usedHighways.add(highway);
    }

    public int calculateTravelTime() {
        return usedHighways.stream().map(highway -> highway.travelTime).reduce(0, Integer::sum);
    }
}
