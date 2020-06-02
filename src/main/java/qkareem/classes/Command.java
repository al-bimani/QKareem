package qkareem.classes;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.util.ArgsStream;

public class Command {
    public String name;
    public String usage;
    public String example;
    public String description;

    public Command(String commandName, String commandUsage, String commandExample, String commandDescription) {
        this.name = commandName;
        this.usage = commandUsage;
        this.example = commandExample;
        this.description = commandDescription;
    }

    public String getName() {
        return name;
    }

    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {};
}