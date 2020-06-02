package qkareem.commands;

import java.io.UnsupportedEncodingException;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.util.ArgsStream;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "%sstop", "%sstop", "stops and clears all queue");
    }

    @Override
    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {
        Bot.player.stopTrack();
        Bot.player.setPaused(false);

        try {
            event.getChannel().sendMessage(new String(".صدق الله العظيم".getBytes(), "UTF-8")).queue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
