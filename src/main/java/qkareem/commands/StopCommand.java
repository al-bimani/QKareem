package qkareem.commands;

import org.json.JSONObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.util.ArgsStream;

public class StopCommand extends Command {

    public StopCommand(JSONObject commandData) {
        super(commandData);
    }

    @Override
    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {
        if (args.peek() != null) {
            event.getChannel().sendMessage(String.format(Bot.locale.get("INVLD_ARGS"), Bot.prefix, this.name)).queue();
            return;
        }

        Bot.player.stopTrack();
        Bot.player.setPaused(false);

        // try {
        event.getChannel().sendMessage(".صدق الله العظيم").queue();
        // } catch (UnsupportedEncodingException e) {
        // e.printStackTrace();
        // }
        super.exec(event, args);
    }
}
