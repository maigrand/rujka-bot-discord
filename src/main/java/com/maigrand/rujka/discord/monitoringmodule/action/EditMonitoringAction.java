package com.maigrand.rujka.discord.monitoringmodule.action;

import com.maigrand.rujka.discord.util.DiscordId;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.payload.discord.MonitoringDetails;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@RequiredArgsConstructor
public class EditMonitoringAction {

    private final MonitoringService monitoringService;

    public void execute(GuildMessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        //m/edit #textChannel serverName param value

        if(args.length < 5) {
            event.getChannel().sendMessage("Usage: cmd <#textChannel> <serverName> <parameter> <value>" +
                    "\n Parameters: name, password").queue();
            return;
        }

        TextChannel textChannel= DiscordId.getTextChannel(event.getGuild(), args[1]);

        if (textChannel == null) {
            event.getChannel().sendMessage("Bad textChannel").queue();
            return;
        }

        MonitoringEntity entity = monitoringService.findByChannelIdAndServerName(textChannel.getId(), args[2]);
        if(entity==null){
            event.getChannel().sendMessage("Entity not found! Bad textChannel + serverName").queue();
            return;
        }

        switch (args[3]){
            case "name":
                entity.setServerName(args[4]);
                break;
            case "password":
                entity.setServerPassword(args[4]);
                break;
        }

        monitoringService.save(entity);
        event.getChannel().sendMessage("Server \"" + args[2] + "\" updated.").queue();
    }
}
