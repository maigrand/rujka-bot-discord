package com.maigrand.rujka.discord.monitoringmodule.action;

import com.maigrand.rujka.discord.util.DiscordId;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@RequiredArgsConstructor
public class MonitoringListAction {

    private final MonitoringService monitoringService;

    public void execute(MessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (MonitoringEntity ent : monitoringService.findAll()) {
            TextChannel textChannel = DiscordId.getTextChannel(event.getGuild(), ent.getChannelId());
            sb.append(textChannel.getAsMention()).append(" : ").append(ent.getServerIndex()).append(") ")
                    .append(ent.getServerName()).append(" (`").append(ent.getServerAddress()).append("`)").append("\n");
        }

        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle("Servers list");
        emb.setDescription(sb);
        event.getChannel().sendMessageEmbeds(emb.build()).queue();
    }
}
