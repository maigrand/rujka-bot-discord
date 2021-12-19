package com.maigrand.rujka.discord.monitoringmodule.action;

import com.maigrand.rujka.discord.util.DiscordId;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@RequiredArgsConstructor
public class RemoveMonitoringAction {

    private final MonitoringService monitoringService;

    public void execute(GuildMessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length != 3) {
            event.getChannel().sendMessage("Usage: cmd <#textChannel> <serverName>").queue();
            return;
        }

        TextChannel textChannel = DiscordId.getTextChannel(event.getGuild(), args[1]);

        if (textChannel == null) {
            event.getChannel().sendMessage("Bad textChannel").queue();
            return;
        }

        MonitoringEntity ent = monitoringService.findByChannelIdAndServerName(textChannel.getId(), args[2]);
        if (ent == null) {
            event.getChannel().sendMessage("server not found").queue();
            return;
        }

        textChannel.retrieveMessageById(ent.getMessageId()).queue(m -> m.delete().queue());
        monitoringService.delete(ent.getId());
        event.getChannel().sendMessage("Deleted.").queue();
    }
}
