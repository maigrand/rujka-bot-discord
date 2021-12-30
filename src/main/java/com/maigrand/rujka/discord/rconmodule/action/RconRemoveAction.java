package com.maigrand.rujka.discord.rconmodule.action;

import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import com.maigrand.rujka.service.discord.rcon.RconService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

@RequiredArgsConstructor
public class RconRemoveAction {

    private final RconService rconService;

    private final List<String> serverNameList;

    public void execute(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length == 1) {
            event.getChannel().sendMessage("Usage: cmd serverIndex or serverName").queue();
            return;
        }

        RconEntity ent = rconService.findByServerNameOrId(args[1]);
        if (ent == null) {
            event.getChannel().sendMessage("Rcon server not found").queue();
            return;
        }
        rconService.delete(ent);
        serverNameList.removeIf(s -> s.equals(ent.getServerName()));
        event.getChannel().sendMessage("Removed.").queue();
    }
}
