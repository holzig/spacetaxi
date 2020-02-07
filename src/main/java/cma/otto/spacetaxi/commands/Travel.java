package cma.otto.spacetaxi.commands;

import picocli.CommandLine.Parameters;

public class Travel {
    @Parameters(index = "0", description = "The start of the trip. Remember to use to put systems with multiple words into brackets. For Example: \"Alpha Centauri\"")
    public String start;
    @Parameters(index = "1", description = "The destination of the trip. Remember to use to put systems with multiple words into brackets. For Example: \"Alpha Centauri\"")
    public String destination;
}
