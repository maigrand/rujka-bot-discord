package com.maigrand.rujka.discord.rconmodule.action;

import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import com.maigrand.rujka.service.discord.rcon.RconService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

@RequiredArgsConstructor
public class RconAddAction {

    private final RconService rconService;

    private final List<String> serverNameList;

    public void execute(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length == 1) {
            event.getChannel().sendMessage("Usage: cmd serverIp:serverPort serverName rconPassword").queue();
            return;
        }

        RconEntity ent = rconService.findByServerName(args[2]);
        if (ent != null) {
            event.getChannel().sendMessage("Already exists.").queue();
            return;
        }

        ent = new RconEntity();
        ent.setServerAddress(args[1]);
        ent.setServerName(args[2]);
        ent.setRconPassword(args[3]);

        rconService.save(ent);
        serverNameList.add(args[2]);

        event.getChannel().sendMessage("Success").queue();
    }
}
