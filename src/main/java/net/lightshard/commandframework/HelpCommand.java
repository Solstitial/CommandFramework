package net.lightshard.commandframework;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends SubCommand
{
    private static final int ELEMENTS_PER_PAGE = 7;
    private static final String ALIAS = "help";
    private static final String DESCRIPTION = "view all commands";

    private final Command parent;

    public HelpCommand(Command parent)
    {
        super(parent, new String[]{ALIAS}, "/" + parent.getAlias() + " help <page>", DESCRIPTION);

        this.parent = parent;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {
        int page = 1;
        if (args.length >= 1)
        {
            try
            {
                page = Integer.parseInt(args[0]);
            }
            catch (Exception ignored) {}
        }

        List<SubCommand> permissible = parent.getSubCommands()
                                                .stream()
                                                .filter(sub -> hasPermission(sender))
                                                .filter(sub-> !hasToBePlayer()
                                                                || hasToBePlayer() && sender instanceof Player)
                                                .collect(Collectors.toList());

        final int pages = (int) Math.ceil((double) permissible.size() / (double) ELEMENTS_PER_PAGE);

        if (page > pages) page = pages;
        if (page < 1) page = 1;

        int startIndex = (page - 1) * ELEMENTS_PER_PAGE;
        int endIndex = startIndex + ELEMENTS_PER_PAGE;
        if (endIndex > permissible.size())
            endIndex = permissible.size() - 1;

        List<SubCommand> subSet = permissible.subList(startIndex, endIndex);
        MessageStore messageStore = parent.getCommandManager().getMessageStore();
        final int finalPage = page;
        messageStore.sendMessage(sender, MessageStore.MessageType.HELP_HEADER,
                                 new HashMap<String, String>()
                                 {{
                                        put("%PAGE%", String.valueOf(finalPage));
                                        put("%PAGES%", String.valueOf(pages));
                                 }});
        for (SubCommand subCommand : subSet)
        {
            messageStore.sendMessage(sender, MessageStore.MessageType.HELP_FORMAT,
                                     new HashMap<String, String>()
                                     {{
                                         put("%EXAMPLE%", subCommand.getExample());
                                         put("%DESCRIPTION%", subCommand.getDescription());
                                     }});
        }
    }

    @Override
    public boolean hasPermission(CommandSender sender)
    {
        return true;
    }

    @Override
    public boolean hasToBePlayer()
    {
        return false;
    }

}
