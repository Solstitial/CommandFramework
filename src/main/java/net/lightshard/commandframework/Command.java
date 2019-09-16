package net.lightshard.commandframework;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Command implements CommandExecutor, TabCompleter
{
    private final CommandManager commandManager;
    private final String alias;
    private final Set<SubCommand> subCommands;

    public Command(CommandManager commandManager, String alias)
    {
        this.commandManager = commandManager;
        this.alias = alias;
        subCommands = new HashSet<SubCommand>(createSubCommands());
        subCommands.add(new HelpCommand(this));
    }

    public abstract Collection<SubCommand> createSubCommands();

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command,
                             String label, String[] args)
    {
        if (args.length > 0)
        {
            for (SubCommand subCommand : subCommands)
            {
                for (String alias : subCommand.getAliases())
                {
                    if (alias.equalsIgnoreCase(args[0]))
                    {
                        String[] newArgs = new String[args.length - 1];
                        for(int i = 1; i < args.length; i ++)
                        {
                            newArgs[i - 1] = args[i];
                        }

                        if(!(subCommand.hasPermission(sender)))
                        {
                            commandManager.getMessageStore().sendMessage(sender,
                                                                         MessageStore.MessageType.NO_PERMISSION);
                            return false;
                        }

                        if(subCommand.hasToBePlayer() && (!(sender instanceof Player)))
                        {
                            commandManager.getMessageStore().sendMessage(sender,
                                                                         MessageStore.MessageType.MUST_BE_INGAME);
                            return false;
                        }

                        subCommand.onCommand(sender, newArgs);
                        return true;
                    }
                }
            }
        }

        sendHelp(sender, 1);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command,
                                      String label, String[] args)
    {
        Collection<SubCommand> filtered;
        if (args.length >= 1)
        {
            String arg0Lower = args[0].toLowerCase();
            filtered = subCommands
                            .stream()
                            .filter(sub -> sub.getMainAlias().toLowerCase().startsWith(arg0Lower))
                            .collect(Collectors.toList());
        }
        else
        {
            filtered = subCommands;
        }
        return filtered.stream().map(SubCommand::getMainAlias).collect(Collectors.toList());
    }

    public void sendHelp(CommandSender sender, int page)
    {
        Bukkit.getServer().dispatchCommand(sender, alias + " help " + page);
    }

    @Override
    public int hashCode()
    {
        return alias.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return (obj != null && obj instanceof Command)
                    ? ((Command) obj).hashCode() == hashCode()
                    : false;
    }

    public CommandManager getCommandManager()
    {
        return commandManager;
    }

    public String getAlias()
    {
        return alias;
    }

    public Set<SubCommand> getSubCommands()
    {
        return subCommands;
    }

}
