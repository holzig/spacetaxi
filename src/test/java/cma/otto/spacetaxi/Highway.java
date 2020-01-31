package cma.otto.spacetaxi;

class Highway {
    final String start;
    final String target;
    final int travelTime;

    Highway(String start, String target, int travelTime) {
        this.start = start;
        this.target = target;
        this.travelTime = travelTime;
    }
}
