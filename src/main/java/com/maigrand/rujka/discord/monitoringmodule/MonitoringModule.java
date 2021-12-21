package com.maigrand.rujka.discord.monitoringmodule;

import com.maigrand.rujka.discord.DiscordModule;
import com.maigrand.rujka.discord.monitoringmodule.action.*;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.MonitoringService;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class MonitoringModule extends DiscordModule {

    private final MonitoringService monitoringService;

    public MonitoringModule(JdaService jdaService, MonitoringService monitoringService) {
        super(jdaService);
        this.monitoringService = monitoringService;
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

        //fixme
        if (!event.getAuthor().getId().equals("154437997989855232")) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

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
    }

    private void monitoringList(GuildMessageReceivedEvent event) {
        ListMonitoringAction action = new ListMonitoringAction(monitoringService);
        action.execute(event);
    }

    private void monitoringAdd(GuildMessageReceivedEvent event) {
        AddMonitoringAction action = new AddMonitoringAction(monitoringService);
        action.execute(event);
    }

    private void monitoringEdit(GuildMessageReceivedEvent event) {
        EditMonitoringAction action = new EditMonitoringAction(monitoringService);
        action.execute(event);
    }

    private void monitoringRemove(GuildMessageReceivedEvent event) {
        RemoveMonitoringAction action = new RemoveMonitoringAction(monitoringService);
        action.execute(event);
    }

    private void monitoringRebuild(GuildMessageReceivedEvent event) {
        RebuildAction action = new RebuildAction(monitoringService);
        action.execute(event);
    }
}
