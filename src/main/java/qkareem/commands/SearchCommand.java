package qkareem.commands;

import java.util.ArrayList;

import org.json.JSONObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.classes.Reciter;
import qkareem.classes.Surah;
import qkareem.util.ArgsStream;

public class SearchCommand extends Command {

    public SearchCommand(JSONObject commandData) {
        super(commandData);
    }

    @Override
    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {

        if (args.peek() == null) {
            event.getChannel().sendMessage(String.format(this.usage, Bot.prefix)).queue();
            return;
        }
        String topic = args.next().value;

        if (args.peek() == null) {
            event.getChannel().sendMessage(String.format(this.usage, Bot.prefix)).queue();
            return;
        }
        
        String query = args.next().value;

        if (args.peek() != null) {
            event.getChannel().sendMessage(String.format(Bot.locale.get("INVLD_ARGS"), Bot.prefix, this.name)).queue();
            return;
        }

        String resultsString = "";

        if (Bot.locale.get("RECITERS").equals(topic)) {
            ArrayList<Reciter> results = Bot.qMp3.searchReciters(query, 65);

            if (results.size() == 0) {
                event.getChannel().sendMessage(String.format(Bot.locale.get("REC_SER_NO_RES"), query)).queue();
                return;
            }

            for (Reciter result : results)
                resultsString += String.format("[%d] = %s -> %s\n", result.id, result.name, result.rewaya);

        } else if (Bot.locale.get("SURAS").equals(topic)) {
            ArrayList<Surah> results = Bot.qMp3.searchSuras(query, 65);

            if (results.size() == 0) {
                event.getChannel().sendMessage(String.format(Bot.locale.get("SUR_SER_NO_RES"), query)).queue();
                return;
            }

            for (Surah result : results)
                resultsString += String.format("[%d] = %s\n", result.id, result.name);
        } else {
            event.getChannel().sendMessage(String.format(Bot.locale.get("INVLD_SER_TOPIC"), topic)).queue();
            return;
        }

        event.getChannel().sendMessage(resultsString).queue();
        super.exec(event, args);
    }
}