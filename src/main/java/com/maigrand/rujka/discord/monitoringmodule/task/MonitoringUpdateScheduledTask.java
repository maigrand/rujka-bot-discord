package com.maigrand.rujka.discord.monitoringmodule.task;

import com.maigrand.rujka.discord.NotifyModule;
import com.maigrand.rujka.discord.monitoringmodule.util.MonitoringMessageUtil;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class MonitoringUpdateScheduledTask {

    private JDA jda;

    private final MonitoringService monitoringService;

    private final JdaService jdaService;

    private final NotifyModule notifyModule;

    @Value("${app.discord.monitoring_update_task.sleep_in_ms}")
    private Integer sleepInMs;

    //@Scheduled(cron = "0 0/5 * * * ?", zone = "Europe/Moscow")
    @Scheduled(fixedDelayString = "${app.discord.monitoring_update_task.fixed_delay_in_ms}", initialDelay = 5000)
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
                Thread.sleep(sleepInMs);
            } catch (Exception e) {
                notifyModule.sendMessage(e.getLocalizedMessage());
            }
        }
    }
}
