package cma.otto.spacetaxi;

import cma.otto.spacetaxi.commands.Commands;
import picocli.CommandLine;

public class App {

    public static void main(String[] args) {
        System.exit(new CommandLine(new Commands()).execute(args));
    }

}
