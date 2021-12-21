package com.maigrand.rujka.discord.monitoringmodule.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

@RequiredArgsConstructor
public class MonitoringExportAction {

    private final MonitoringService monitoringService;

    public void execute(GuildMessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }

        List<MonitoringEntity> all = monitoringService.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("```");
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(all));
            sb.append("```");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        event.getChannel().sendMessage(sb).queue();
    }
}
