package qkareem.commands;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.json.JSONObject;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.classes.Reciter;
import qkareem.classes.Surah;
import qkareem.listeners.AudioPlayerSendHandler;
import qkareem.util.ArgsStream;

public class PlayCommand extends Command {

    private static Surah surah = null;
    private static Reciter reciter = null;

    public PlayCommand(JSONObject commandData) {
        super(commandData);
    }

    @Override
    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {

        if (args.peek() == null) {
            event.getChannel().sendMessage(String.format(this.usage, Bot.prefix, Bot.prefix)).queue();
            return;
        }
        String surahName = args.next().value;

        if (args.peek() == null) {
            event.getChannel().sendMessage(String.format(this.usage, Bot.prefix, Bot.prefix)).queue();
            return;
        }
        String reciterName = args.next().value;

        String rewaya = "";
        if (args.peek() != null) {
            rewaya = args.next().value;
        }

        if (args.peek() != null) {
            event.getChannel().sendMessage(String.format(Bot.locale.get("INVLD_ARGS"), Bot.prefix, this.name)).queue();
            return;
        }

        ArrayList<Reciter> reciterSearchResults = Bot.qMp3.searchReciters(reciterName, 55);
        if (reciterSearchResults.size() == 0) {
            event.getChannel().sendMessage(String.format(Bot.locale.get("REC_SER_NO_RES"), reciterName)).queue();
            return;
        }
        reciter = reciterSearchResults.get(0);

        ArrayList<Surah> surasSearchResult = Bot.qMp3.searchSuras(surahName, 55);
        if (surasSearchResult.size() == 0) {
            event.getChannel().sendMessage(String.format(Bot.locale.get("SUR_SER_NO_RES"), surahName)).queue();
            return;
        }
        surah = surasSearchResult.get(0);

        Reciter surahReciter = null;
        if (rewaya.isEmpty()) { // if no rewaya specified search for any avaliable
            for (Reciter _reciter : reciterSearchResults) {
                if (_reciter.name.equals(reciter.name) && _reciter.hasSurah(surah.id)) { // same name but diffrent
                                                                                         // rewaya
                    surahReciter = _reciter;
                    break;
                } else
                    surahReciter = null;
            }
        } else {
            for (Reciter _reciter : reciterSearchResults) {
                if (_reciter.name.equals(reciter.name) && _reciter.recitesRewaya(rewaya)
                        && _reciter.hasSurah(surah.id)) {
                    surahReciter = _reciter;
                    break;
                } else
                    surahReciter = null;
            }
        }

        if (surahReciter == null) {
            if (rewaya.isEmpty())
                event.getChannel()
                        .sendMessage(String.format(Bot.locale.get("REC_SUR_NO_RES"), surah.name, reciter.name)).queue();
            else
                event.getChannel().sendMessage(
                        String.format(Bot.locale.get("REC_SUR_WITH_REW_NO_RES"), surah.name, reciter.name, rewaya))
                        .queue();
            return;
        } else
            reciter = surahReciter;

        String mp3Url = reciter.getSurahMp3Url(surah.id);

        Guild guild = event.getChannel().getGuild();
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(Bot.player));

        AudioLoadResultHandler onload = new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                VoiceChannel vchannel = event.getMember().getVoiceState().getChannel();
                if (vchannel == null)
                    return;
                event.getChannel()
                        .sendMessage(
                                String.format(Bot.locale.get("QUEUE_ADD"), surah.name, reciter.name, reciter.rewaya))
                        .queue();
                Bot.play(event.getChannel().getGuild(), vchannel, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage("Nothing found by " + mp3Url).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getChannel().sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        };
        Bot.playerManager.loadItem(mp3Url, onload);
        super.exec(event, args);
    }
}