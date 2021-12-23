package com.maigrand.rujka.discord.monitoringmodule.task;

import com.maigrand.rujka.discord.monitoringmodule.util.MonitoringMessageUtil;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MonitoringUpdateScheduledTask {

    private JDA jda;

    private final MonitoringService monitoringService;

    private final JdaService jdaService;

    @Scheduled(cron = "0 0/1 * * * ?", zone = "Europe/Moscow")
    private void execute() {
        if (jda == null) {
            jda = jdaService.getJda();
        }

        //todo: optimize
        List<MonitoringEntity> all = monitoringService.findAll();
        for (MonitoringEntity ent : all) {
            Guild guildById = jda.getGuildById(ent.getGuildId());
            if (guildById == null) {
                return;
            }
            TextChannel textChannelById = guildById.getTextChannelById(ent.getChannelId());
            if (textChannelById == null) {
                return;
            }

            textChannelById.retrieveMessageById(ent.getMessageId()).queue(msg -> {
                MonitoringMessageUtil monitoringMessageUtil = new MonitoringMessageUtil(ent);
                monitoringMessageUtil.update(msg);
            }, throwable -> {
                System.out.println(throwable.getMessage());
                System.out.println(guildById.getName() + " : " + textChannelById.getName() + " : " + ent.getServerName());
            });
        }
    }
}
