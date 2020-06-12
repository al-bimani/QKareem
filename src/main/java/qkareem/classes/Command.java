package qkareem.classes;

import org.json.JSONObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qkareem.util.ArgsStream;

public class Command {
    public String name;
    public String usage;
    public String example;
    public String description;

    public Command(JSONObject commandData) {
        this.name = commandData.getString("name");
        this.usage = commandData.getString("usage");
        this.example = commandData.getString("example");
        this.description = commandData.getString("description");
    }

    public String getName() {
        return name;
    }

    public void exec(GuildMessageReceivedEvent event, ArgsStream args) {
    }

}