package cma.otto.spacetaxi.commands;

import cma.otto.spacetaxi.DefaultDataset;
import cma.otto.spacetaxi.Route;
import cma.otto.spacetaxi.RouteFinder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;

@Command(
        name = "max",
        mixinStandardHelpOptions = true,
        description = "Finds routes that fit the specified maximum value."
)
public class MaxCommand extends TravelCommand {

    @Option(names = "--type", defaultValue = "time", required = true, description = "Search either by travel time or stops.")
    Type type;
    @Option(names = "--value", required = true, description = "The maximum amount of the given type.")
    Integer value;

    @Override
    protected List<Route> findRoutes(Travel travel) {
        RouteFinder routeFinder = new RouteFinder(DefaultDataset.highways);

        if (type.equals(Type.stops)) {
            return routeFinder.findRouteWithMaxStops(travel.start, travel.destination, value);
        } else {
            return routeFinder.findRoutesWithMaxTravelTime(travel.start, travel.destination, value);
        }
    }


    protected void printHeaderMessage(Travel travel) {
        String typeString = type.equals(Type.stops) ? "stops" : "days travel time";
        System.out.println(String.format("Routes with a maximum of %s %s between %s and %s:", value, typeString, travel.start, travel.destination));
    }
}
