package com.maigrand.rujka.discord.rconmodule.action;

import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import com.maigrand.rujka.service.discord.rcon.RconService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@RequiredArgsConstructor
public class RconEditAction {

    private final RconService rconService;

    public void execute(GuildMessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        //rcon/edit <serverName or id> param value

        if (args.length < 4) {
            event.getChannel().sendMessage("Usage: cmd <serverName or id> <parameter> <value>" +
                    "\n Parameters: address, name, password").queue();
            return;
        }

        RconEntity ent = rconService.findByServerNameOrId(args[1]);
        if (ent == null) {
            event.getChannel().sendMessage("Rcon server not found").queue();
            return;
        }

        switch (args[2]) {
            case "address":
                ent.setServerAddress(args[3]);
                break;
            case "name":
                ent.setServerName(args[3]);
                break;
            case "password":
                ent.setRconPassword(args[3]);
                break;
        }

        rconService.save(ent);
        event.getChannel().sendMessage("Server \"" + ent.getServerName() + "\" updated.").queue();
    }
}
