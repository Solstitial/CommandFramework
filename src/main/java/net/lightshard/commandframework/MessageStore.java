package net.lightshard.commandframework;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MessageStore
{
    private Map<MessageType, String> cache;

    public MessageStore()
    {
        cache = new HashMap<MessageType, String>();
    }

    public void sendMessage(CommandSender sender, MessageType type)
    {
        sender.sendMessage(get(type));
    }

    public void sendMessage(CommandSender sender, MessageType type,
                            Map<String, String> placeHolderMappings)
    {
        String message = get(type);
        for(Map.Entry<String, String> entry : placeHolderMappings.entrySet())
        {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        sender.sendMessage(message);
    }

    public void set(MessageType type, String message)
    {
        cache.put(type, ChatColor.translateAlternateColorCodes('&', message));
    }

    public void setDefaults()
    {
        set(MessageType.NO_PERMISSION, "&cYou do not have permission to do this.");
        set(MessageType.MUST_BE_INGAME, "&cYou must be in-game to run this command.");
        set(MessageType.HELP_HEADER, "&e------------------- Page %PAGE% of %PAGES% -------------------");
        set(MessageType.HELP_FORMAT, "&b%EXAMPLE% &f - %DESCRIPTION%");
    }

    public String get(MessageType type)
    {
        return cache.get(type);
    }

    public enum MessageType
    {
        NO_PERMISSION,
        MUST_BE_INGAME,
        HELP_HEADER,
        HELP_FORMAT;
    }

}
