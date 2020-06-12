package qkareem.commands;

import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.util.ArgsStream;

public class HelpCommand extends Command {
    public HelpCommand(JSONObject commandData) {
        super(commandData);
    }

    @Override
    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {
        String helpMessage = "";
        EmbedBuilder embed = new EmbedBuilder();
        if (args.peek() != null) {
            String commandName = args.next().value;
            for (Command command : Bot.commands) {
                if (command.name.equals(commandName)) {
                    embed.setAuthor(String.format(Bot.locale.get("CMD_HELP_MSG_TITLE"), command.name));
                    helpMessage += String.format(Bot.locale.get("DESC"), command.description);
                    helpMessage += "\n";
                    helpMessage += String.format(Bot.locale.get("USAGE"),
                    String.format(command.usage, Bot.prefix, Bot.prefix, Bot.prefix));
                    helpMessage += "\n";
                    helpMessage += String.format(Bot.locale.get("EXAMP"),
                            String.format(command.example, Bot.prefix, Bot.prefix, Bot.prefix, Bot.prefix, Bot.prefix));
                    embed.setDescription(helpMessage);
                }
            }
            if (helpMessage.isEmpty())
                embed.setDescription(String.format(Bot.locale.get("NO_SUC_CMD"), commandName));
        } else {
            embed.setAuthor(Bot.locale.get("HELP_MSG_TITLE"));
            for (Command command : Bot.commands) {
                helpMessage += String.format("%s\n%s\n\n", String.format(Bot.locale.get("NAME"), command.name),
                        String.format(Bot.locale.get("DESC"), command.description));
            }
            helpMessage += String.format(Bot.locale.get("MRE_INF"), this.name, Bot.prefix);
            embed.setDescription(helpMessage);
        }
        event.getChannel().sendMessage(embed.build()).queue();
    }
}