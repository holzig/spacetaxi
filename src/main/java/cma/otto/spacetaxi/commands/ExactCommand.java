package cma.otto.spacetaxi.commands;

import cma.otto.spacetaxi.DefaultDataset;
import cma.otto.spacetaxi.Route;
import cma.otto.spacetaxi.RouteFinder;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(
        name = "exact",
        mixinStandardHelpOptions = true,
        description = "Finds routes that fit the specified exact amount of stops."
)
public class ExactCommand extends TravelCommand {

    @CommandLine.Option(names = "--value", required = true, description = "The exact amount of steps that the routes need to have.")
    Integer value;

    @Override
    protected List<Route> findRoutes(Travel travel) {
        RouteFinder routeFinder = new RouteFinder(DefaultDataset.highways);
        return routeFinder.findRoutesWithExactStops(travel.start, travel.destination, value);
    }

    protected void printHeaderMessage(Travel travel) {
        System.out.println(String.format("Routes with exact %s steps between %s and %s:", value, travel.start, travel.destination));
    }
}
