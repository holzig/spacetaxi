package cma.otto.spacetaxi;

import java.util.Objects;

class Highway {
    final String start;
    final String target;
    final int travelTime;

    Highway(String start, String target, int travelTime) {
        this.start = start;
        this.target = target;
        this.travelTime = travelTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Highway highway = (Highway) o;
        return travelTime == highway.travelTime &&
                Objects.equals(start, highway.start) &&
                Objects.equals(target, highway.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, target, travelTime);
    }
}
