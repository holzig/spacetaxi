package cma.otto.spacetaxi.commands;

import cma.otto.spacetaxi.DefaultDataset;
import cma.otto.spacetaxi.NoSuchRouteException;
import cma.otto.spacetaxi.Route;
import cma.otto.spacetaxi.RouteParser;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
        name = "path",
        mixinStandardHelpOptions = true,
        description = "Detects if given paths are valid routes."
)
public class PathCommand implements Callable<Integer> {
    @Parameters(
            arity = "1..*",
            description = "Multiple paths are supported. Each Path should must contain at least two systems. The Systems need to be separated by \"->\"."
    )
    String[] paths;

    @Override
    public Integer call() {
        RouteParser routeParser = new RouteParser(DefaultDataset.highwaysByStartSystem);
        int exitCode = 0;
        for (String path : paths) {
            try {
                Route route = routeParser.parse(path);
                System.out.println(route);
            } catch (NoSuchRouteException e) {
                System.err.println("No such Route: " + path);
                exitCode = 1;
            }
        }
        return exitCode;
    }
}
