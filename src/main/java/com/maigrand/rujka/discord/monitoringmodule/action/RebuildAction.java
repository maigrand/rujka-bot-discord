package com.maigrand.rujka.discord.monitoringmodule.action;

import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

@RequiredArgsConstructor
public class RebuildAction {

    private final MonitoringService monitoringService;

    public void execute(GuildMessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }

        List<MonitoringEntity> monitoringEntityList = monitoringService.findByGuildId(event.getGuild().getId());

        for (MonitoringEntity entity : monitoringEntityList) {
            TextChannel textChannel = event.getGuild().getTextChannelById(entity.getChannelId());
            textChannel.retrieveMessageById(entity.getMessageId()).queue(m -> m.delete().queue());
            textChannel.sendMessageEmbeds(new EmbedBuilder().setTitle(entity.getServerName()).build()).queue(msg -> {
                entity.setMessageId(msg.getId());
                monitoringService.save(entity);
            });
        }
    }
}
