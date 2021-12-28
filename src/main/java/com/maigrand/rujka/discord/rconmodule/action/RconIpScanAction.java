package com.maigrand.rujka.discord.rconmodule.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maigrand.rujka.entity.discord.rcon.RconIpScanIpEntity;
import com.maigrand.rujka.entity.discord.rcon.RconIpScanNameEntity;
import com.maigrand.rujka.service.discord.rcon.RconIpScanIpService;
import com.maigrand.rujka.service.discord.rcon.RconIpScanNameService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RconIpScanAction {

    private final RconIpScanIpService ipService;

    private final RconIpScanNameService ipScanNameService;

    public void execute(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length == 1) {
            event.getChannel().sendMessage("Usage: rcon/ip nickname/ip/all").queue();
            return;
        }

        List<Map<String, Set<String>>> ipScanList;

        if (args[1].equals("all")) {
            ipScanList = rconIpSearch(ipScanNameService.findAll());
            sendFile(event, ipScanList);
            return;
        }

        if (args[1].matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            RconIpScanIpEntity byIp = ipService.findByIp(args[1]);
            if (byIp == null) {
                event.getChannel().sendMessage("Not found.").queue();
                return;
            }
            ipScanList = rconIpSearch(new ArrayList<>(byIp.getIpScanNameEntitySet()));
            sendFile(event, ipScanList);
            return;
        }

        List<RconIpScanNameEntity> byName = ipScanNameService.findByNameContains(args[1]);
        if (byName != null && !byName.isEmpty()) {
            ipScanList = rconIpSearch(byName);
            sendFile(event, ipScanList);
        } else {
            event.getChannel().sendMessage("Not found.").queue();
        }
    }

    private void sendFile(GuildMessageReceivedEvent event, List<Map<String, Set<String>>> ipScanList) {
        try {
            File tempFile = File.createTempFile("iplog", ".json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, ipScanList);
            event.getChannel().sendFile(tempFile).queue();
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            event.getChannel().sendMessage(e.getLocalizedMessage()).queue();
        }
    }

    private List<Map<String, Set<String>>> rconIpSearch(List<RconIpScanNameEntity> ipScanEntityList) {
        List<Map<String, Set<String>>> ipScanList = new ArrayList<>();

        Map<String, Set<String>> map = new HashMap<>();

        for (RconIpScanNameEntity ipScanNameEntity : ipScanEntityList) {
            Set<String> ips = ipScanNameEntity.getIpScanIpEntitySet().stream()
                    .map(RconIpScanIpEntity::getIp)
                    .collect(Collectors.toSet());
            map.put(ipScanNameEntity.getName(), ips);
        }

        ipScanList.add(map);

        return ipScanList;
    }
}
