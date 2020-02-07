package cma.otto.spacetaxi.commands;

import cma.otto.spacetaxi.DefaultDataset;
import cma.otto.spacetaxi.Route;
import cma.otto.spacetaxi.RouteFinder;
import picocli.CommandLine.Command;

import java.util.List;

@Command(
        name = "shortest",
        mixinStandardHelpOptions = true,
        description = "Finds the shortest route in travel time between two Systems"
)
public class ShortestCommand extends TravelCommand {

    protected List<Route> findRoutes(Travel travel) {
        RouteFinder routeFinder = new RouteFinder(DefaultDataset.highways);
        return routeFinder.findShortestRoute(travel.start, travel.destination);
    }

    protected void printHeaderMessage(Travel travel) {
        System.out.println(String.format("Shortest routes between %s and %s:", travel.start, travel.destination));
    }
}
