package com.maigrand.rujka.discord.rconmodule;

import com.maigrand.rujka.discord.DiscordModule;
import com.maigrand.rujka.discord.rconmodule.action.*;
import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.rcon.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

//todo: вынести textChannelId в бд
@Component
public class RconModule extends DiscordModule {

    private List<String> serverNameList;

    private final RconService rconService;

    private final RconIpScanIpService rconIpScanIpService;

    private final RconIpScanNameService rconIpScanNameService;

    @Value("${app.discord.module.rcon.text_channel.id}")
    private String rconTextChannelId;

    public RconModule(JdaService jdaService,
            RconService rconService,
            RconIpScanIpService rconIpScanIpService,
            RconIpScanNameService rconIpScanNameService) {
        super(jdaService);
        this.rconService = rconService;
        this.rconIpScanIpService = rconIpScanIpService;
        this.rconIpScanNameService = rconIpScanNameService;
    }

    @PostConstruct
    private void init() {
        registerPermission(new String[]{
                "RCON_MANAGER",
                "RCON_USE",
                "RCON_IP"
        });
        serverNameList = rconService.findAll()
                .stream()
                .map(RconEntity::getServerName)
                .collect(Collectors.toList());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (event.getMember() == null) {
            return;
        }

        if (event.getAuthor().isBot()) {
            return;
        }

        if (!event.getMessage().getContentRaw().startsWith("rcon")) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equals("rcon/ip")) {
            rconIp(event);
            return;
        }

        if (args[0].equals("rcon/add")) {
            rconAdd(event);
            return;
        }

        if (args[0].equals("rcon/edit")) {
            rconEdit(event);
            return;
        }

        if (args[0].equals("rcon/remove")) {
            rconRemove(event);
            return;
        }

        if (event.getMessage().getContentRaw().equals("rcon/")
                || event.getMessage().getContentRaw().equals("rcon/help")
                || event.getMessage().getContentRaw().equals("rcon/info")) {
            rconInfo(event);
            return;
        }

        if (!event.getChannel().getId().equals(rconTextChannelId)) {
            return;
        }

        if (args[0].equals("rcon/list")) {
            rconList(event);
            return;
        }

        if (event.getMessage().getContentRaw().startsWith("rcon/")) {
            rconExecute(event);
        }
    }

    private void rconInfo(MessageReceivedEvent event) {
        RconInfoAction action = new RconInfoAction();
        action.execute(event);
    }

    private void rconAdd(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "RCON_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        RconAddAction action = new RconAddAction(rconService, serverNameList);
        action.execute(event);
    }

    private void rconEdit(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "RCON_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        RconEditAction action = new RconEditAction(rconService);
        action.execute(event);
    }

    private void rconExecute(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "RCON_USE");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        RconExecuteAction action = new RconExecuteAction(rconService, serverNameList);
        action.execute(event);
    }

    private void rconList(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "RCON_USE");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        RconListAction action = new RconListAction(rconService);
        action.execute(event);
    }

    private void rconRemove(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "RCON_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }
        RconRemoveAction action = new RconRemoveAction(rconService, serverNameList);
        action.execute(event);
    }

    private void rconIp(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "RCON_IP");
        if (!allowed) {
            return;
        }
        RconIpScanAction action = new RconIpScanAction(rconIpScanIpService, rconIpScanNameService);
        action.execute(event);
    }
}
