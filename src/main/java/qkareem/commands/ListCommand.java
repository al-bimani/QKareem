package qkareem.commands;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.classes.Reciter;
import qkareem.classes.Surah;
import qkareem.util.ArgsStream;

public class ListCommand extends Command {
    public ListCommand(JSONObject commandData) {
        super(commandData);
    }

    @Override
    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {
        if (args.peek() == null) {
            event.getChannel().sendMessage(String.format(this.usage, Bot.prefix)).queue();
            return;
        }
        String topic = args.next().value;

        if (topic.equals(String.format(Bot.locale.get("RECITERS")))) {
            String pageStr = args.peek() == null ? "1" : args.next().value;
            if (!pageStr.chars().allMatch(Character::isDigit)) {
                event.getChannel().sendMessage(String.format(Bot.locale.get("PAG_MST_STR"))).queue();
                return;
            }
            int pageNumber = Integer.parseInt(pageStr) - 1;
            List<List<Reciter>> chunks = ListUtils.partition(Bot.qMp3.reciters, 10);

            if (pageNumber >= chunks.size()) {
                event.getChannel().sendMessage(String.format(Bot.locale.get("PAG_NOT_FND"))).queue();
                return;
            }

            List<Reciter> page = chunks.get(pageNumber);
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor(String.format(Bot.locale.get("REC_LST"), pageStr, chunks.size()));
            String pageRepr = "";
            for (Reciter reciter : page) {

                pageRepr += String.format("[%s]: %s • %s", reciter.id, reciter.name, reciter.rewaya);
                pageRepr += "\n";
            }
            embed.setDescription(pageRepr);
            event.getChannel().sendMessage(embed.build()).queue();
        } else if (topic.equals(String.format(Bot.locale.get("SURAS")))) {
            String pageStr = args.peek() == null ? "1" : args.next().value;
            if (!pageStr.chars().allMatch(Character::isDigit)) {
                event.getChannel().sendMessage(String.format(Bot.locale.get("PAG_MST_STR"))).queue();
                return;
            }
            int pageNumber = Integer.parseInt(pageStr) - 1;
            List<List<Surah>> chunks = ListUtils.partition(Bot.qMp3.suras, 10);

            if (pageNumber >= chunks.size()) {
                event.getChannel().sendMessage(String.format(Bot.locale.get("PAG_NOT_FND"))).queue();
                return;
            }

            List<Surah> page = chunks.get(pageNumber);
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor(String.format(Bot.locale.get("SUR_LST"), pageStr, chunks.size()));
            String pageRepr = "";
            for (Surah surah : page) {
                pageRepr += String.format("[%s]: %s", surah.id, surah.name);
                pageRepr += "\n";
            }
            embed.setDescription(pageRepr);
            event.getChannel().sendMessage(embed.build()).queue();
        } else {
            event.getChannel().sendMessage(String.format(this.usage, Bot.prefix)).queue();
        }
    }
}