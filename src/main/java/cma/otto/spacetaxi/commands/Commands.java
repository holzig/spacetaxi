package cma.otto.spacetaxi.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.util.concurrent.Callable;

//TODO: All commands need more tests

/**
 * Main command for the App. All other Commands are loaded via this.
 */
@Command(
        name = "taxi",
        synopsisSubcommandLabel = "COMMAND",
        mixinStandardHelpOptions = true,
        subcommands = {PathCommand.class, ShortestCommand.class, MaxCommand.class, ExactCommand.class}
)
public class Commands implements Callable<Integer> {
    @Spec
    private CommandSpec spec;

    @Override
    public Integer call() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}
