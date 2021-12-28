package com.maigrand.rujka.discord.monitoringmodule;

import com.maigrand.rujka.discord.DiscordModule;
import com.maigrand.rujka.discord.monitoringmodule.action.*;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.MonitoringService;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MonitoringModule extends DiscordModule {

    private final MonitoringService monitoringService;

    public MonitoringModule(JdaService jdaService, MonitoringService monitoringService) {
        super(jdaService);
        this.monitoringService = monitoringService;
    }

    @PostConstruct
    public void init() {
        registerPermission(new String[]{
                "MONITORING_MANAGER"
        });
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        if (event.getMember() == null) {
            return;
        }

        if (event.getAuthor().isBot()) {
            return;
        }

        if (!event.getMessage().getContentRaw().startsWith("m/")) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equals("m/help")) {
            monitoringInfo(event);
            return;
        }

        if (args[0].equals("m/add")) {
            monitoringAdd(event);
            return;
        }

        if (args[0].equals("m/edit")) {
            monitoringEdit(event);
            return;
        }

        if (args[0].equals("m/list")) {
            monitoringList(event);
            return;
        }

        if (args[0].equals("m/remove")) {
            monitoringRemove(event);
            return;
        }

        if (args[0].equals("m/rebuild")) {
            monitoringRebuild(event);
            return;
        }

        if (args[0].equals("m/export")) {
            monitoringExport(event);
            return;
        }

        //todo: import json
    }

    private void monitoringInfo(GuildMessageReceivedEvent event) {
        MonitoringInfoAction action = new MonitoringInfoAction();
        action.execute(event);
    }

    private void monitoringList(GuildMessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "MONITORING_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        MonitoringListAction action = new MonitoringListAction(monitoringService);
        action.execute(event);
    }

    private void monitoringAdd(GuildMessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "MONITORING_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        MonitoringAddAction action = new MonitoringAddAction(monitoringService);
        action.execute(event);
    }

    private void monitoringEdit(GuildMessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "MONITORING_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        MonitoringEditAction action = new MonitoringEditAction(monitoringService);
        action.execute(event);
    }

    private void monitoringRemove(GuildMessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "MONITORING_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        MonitoringRemoveAction action = new MonitoringRemoveAction(monitoringService);
        action.execute(event);
    }

    private void monitoringRebuild(GuildMessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "MONITORING_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        MonitoringRebuildAction action = new MonitoringRebuildAction(monitoringService);
        action.execute(event);
    }

    private void monitoringExport(GuildMessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "MONITORING_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        MonitoringExportAction action = new MonitoringExportAction(monitoringService);
        action.execute(event);
    }
}
