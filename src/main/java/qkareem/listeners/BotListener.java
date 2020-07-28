package qkareem.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import qkareem.Bot;
import qkareem.classes.Command;
import qkareem.util.ArgsStream;

public class BotListener extends ListenerAdapter {
    private String prefix;
    private String content;
    private ArgsStream args;
    private String commandName;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        prefix = Bot.prefix;
        content = event.getMessage().getContentRaw();

        if (event.getAuthor().isBot() || event.isWebhookMessage())
            return;

        if (content.indexOf(prefix) != 0)
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

    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild : Bot.getClient().getGuilds()) {
            if (!guild.getId().equals(Bot.main_guild_id)) {
                guild.leave().queue();
            }
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (!event.getGuild().getId().equals(Bot.main_guild_id)) {
            event.getGuild().leave().queue();
        }
    }

    private Member clientMember;
    private VoiceChannel clientVoiceChannel;

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        clientMember = event.getGuild().getMember(Bot.getClient().getSelfUser());
        clientVoiceChannel = clientMember.getVoiceState().getChannel();
        if (clientVoiceChannel != null) {
            if (clientVoiceChannel.getMembers().size() <= 1) {
                Bot.player.setPaused(true);
                System.out.println("Paused");
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        clientMember = event.getGuild().getMember(Bot.getClient().getSelfUser());
        clientVoiceChannel = clientMember.getVoiceState().getChannel();
        if (clientVoiceChannel != null) {
            if (event.getChannelJoined().getId().equals(clientVoiceChannel.getId())) {
                if (Bot.player.isPaused()) {
                    Bot.player.setPaused(false);
                    System.out.println("Resumed");
                }
            }
        }
    }
}