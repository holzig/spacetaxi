package cma.otto.spacetaxi;

import java.util.List;

public interface RouteCondition {

    boolean check(Route route, List<Route> routes);

}
