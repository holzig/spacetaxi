package cma.otto.spacetaxi.commands;

import cma.otto.spacetaxi.Route;
import picocli.CommandLine;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class TravelCommand implements Callable<Integer> {
    @CommandLine.Mixin
    Travel travel;

    @Override
    public Integer call() {
        List<Route> routes = findRoutes(travel);
        if (routes.isEmpty()) {
            System.out.println(String.format("No Routes Found from %s to %s", travel.start, travel.destination));
        } else {
            printHeaderMessage(travel);
            routes.forEach(System.out::println);
        }
        return 0;
    }

    protected abstract void printHeaderMessage(Travel travel);

    protected abstract List<Route> findRoutes(Travel travel);
}
