package net.lightshard.commandframework;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class CommandManager
{
    private final JavaPlugin plugin;
    private final Set<Command> registered;
    private final MessageStore messageStore;

    public CommandManager(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.registered = new HashSet<Command>();
        messageStore = new MessageStore();

        messageStore.setDefaults();
    }

    public boolean registerCommand(Command command)
    {
        if(plugin.getCommand(command.getAlias()) == null)
        {
            plugin.getLogger().log(Level.SEVERE,
                              "Command " + command.getAlias() + " was not in plugin.yml");
            return false;
        }

        plugin.getCommand(command.getAlias()).setExecutor(command);
        plugin.getCommand(command.getAlias()).setTabCompleter(command);
        return true;
    }

    public Set<Command> getRegistered()
    {
        return registered;
    }

    public MessageStore getMessageStore()
    {
        return messageStore;
    }

}
