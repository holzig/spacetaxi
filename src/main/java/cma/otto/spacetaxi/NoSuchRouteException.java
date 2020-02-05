package cma.otto.spacetaxi;

public class NoSuchRouteException extends IllegalArgumentException {

    public NoSuchRouteException() {
        super("NO SUCH ROUTE");
    }

}
