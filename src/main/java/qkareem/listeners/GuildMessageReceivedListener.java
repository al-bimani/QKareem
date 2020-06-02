package qkareem.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.util.ArgsStream;

public class GuildMessageReceivedListener extends ListenerAdapter {
    private String prefix;
    private String content;
    private ArgsStream args;
    private String commandName;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        prefix = Bot.prefix;
        content = event.getMessage().getContentRaw();

        if (!content.substring(0, prefix.length()).equals(prefix))
            return;

        args = new ArgsStream(content.replace(prefix, ""));
        commandName = args.next().value;
        for (Command botCommand : Bot.commands) {
            if (botCommand.getName().equals(commandName)) {
                botCommand.exec(event, args);
            }
        }
        super.onGuildMessageReceived(event);
    }
}