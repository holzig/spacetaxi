package cma.otto.spacetaxi.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.util.concurrent.Callable;

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
