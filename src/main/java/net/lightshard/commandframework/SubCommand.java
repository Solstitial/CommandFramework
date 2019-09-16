package net.lightshard.commandframework;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand
{
    private final Command  command;
    private final String[] aliases;
    private final String   mainAlias;
    private final String   example;
    private final String   description;

    public SubCommand(Command command, String[] aliases,
                      String example, String description)
    {
        this.command = command;
        this.aliases = aliases;
        mainAlias = aliases[0];
        this.example = example;
        this.description = description;
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    public abstract boolean hasPermission(CommandSender sender);

    public abstract boolean hasToBePlayer();

    public Command getCommand()
    {
        return command;
    }

    public String[] getAliases()
    {
        return aliases;
    }

    public String getMainAlias()
    {
        return mainAlias;
    }

    public String getExample()
    {
        return example;
    }

    public String getDescription()
    {
        return description;
    }


}
