package qkareem.commands;

import java.io.UnsupportedEncodingException;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.classes.Reciter;
import qkareem.classes.Surah;
import qkareem.listeners.AudioPlayerSendHandler;
import qkareem.util.ArgsStream;

public class PlayCommand extends Command {

    public PlayCommand() throws UnsupportedEncodingException {
        super(
            "play",
            "%splay <surah number> <\"reciter name\">\nnote: escape the reciter name with double quotes",
            "%splay 3 " + new String("يوسف بن نوح أحمد".getBytes(), "UTF-8"),
            "plays specified surah with specified reciter voice"
        );
    }

    @Override
    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {


        if (!args.peek().value.chars().allMatch(Character::isDigit)) 
            return; // TODO: send usage
        
        int surahId = Integer.parseInt(args.next().value);
        if (surahId <= 0)
            return; // TODO: send usage

        String reciterName = args.next().value;
        event.getChannel().sendMessage(reciterName).queue();
        if (reciterName.isEmpty())
            return; // TODO: send usage

        Reciter reciter = Bot.qMp3.getReciter(reciterName);
        if (reciter == null) {
            event.getChannel().sendMessage("reciter: " + reciterName + " not Found").queue();
            return;
        }

        Surah surah = Bot.qMp3.getReciterSurahById(reciter, surahId);
        if (surah == null) {
            event.getChannel().sendMessage("Surah with id: " + surahId + "for reciter: " + reciterName + " not Found")
                    .queue();
            return;
        }

        String mp3Url = reciter.getSurahMp3Url(surahId);
        event.getChannel().sendMessage(mp3Url).queue();

        Guild guild = event.getChannel().getGuild();
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(Bot.player));

        AudioLoadResultHandler onload = new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                event.getChannel().sendMessage("Adding to queue " + track.getInfo().title).queue();

                Bot.play(event.getChannel().getGuild(), track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // TODO Auto-generated method stub

            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage("Nothing found by " + mp3Url).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                // TODO Auto-generated method stub

            }
        };
        Bot.playerManager.loadItem(mp3Url, onload);
        super.exec(event, args);
    }
}