package com.maigrand.rujka.discord.monitoringmodule.task;

import com.maigrand.rujka.discord.NotifyModule;
import com.maigrand.rujka.discord.monitoringmodule.util.MonitoringMessageUtil;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MonitoringUpdateScheduledTask {

    private JDA jda;

    private final MonitoringService monitoringService;

    private final JdaService jdaService;

    private final NotifyModule notifyModule;

    @Scheduled(cron = "0 0/1 * * * ?", zone = "Europe/Moscow")
    //@Scheduled(fixedRate = 10000)
    private void execute() {
        if (jda == null) {
            jda = jdaService.getJda();
        }

        //todo: optimize
        List<MonitoringEntity> monitoringEntityList = monitoringService.findAll();
        for (MonitoringEntity ent : monitoringEntityList) {
            try {
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
                    notifyModule.sendMessage(throwable.getLocalizedMessage());
                    log.warn("MonitoringUpdateScheduledTask: " + guildById.getName() + " : " + textChannelById.getName() + " : " + ent.getServerName());
                });
            } catch (Exception e) {
                notifyModule.sendMessage(e.getLocalizedMessage());
            }
        }
    }
}
