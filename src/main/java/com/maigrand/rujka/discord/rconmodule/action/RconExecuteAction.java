package com.maigrand.rujka.discord.rconmodule.action;

import com.maigrand.rujka.discord.rconmodule.util.RconEmbedUtil;
import com.maigrand.rujka.discord.rconmodule.util.RconUtil;
import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import com.maigrand.rujka.service.discord.rcon.RconService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

@RequiredArgsConstructor
public class RconExecuteAction {

    private final RconService rconService;

    private final List<String> serverNameList;

    public void execute(GuildMessageReceivedEvent event) {
        try {
            prepare(event);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            event.getChannel().sendMessage(e.getLocalizedMessage()).queue();
        }
    }

    private void prepare(GuildMessageReceivedEvent event) throws SocketException, UnknownHostException {
        if (serverNameList != null) {
            if (!serverNameList.isEmpty()) {
                String[] args = event.getMessage().getContentRaw().split("\\s+");
                String cmdServerName = args[0].substring("rcon/".length());

                for (String serverName : serverNameList) {
                    if (cmdServerName.equals(serverName)) {
                        sendCommand(serverName, event);
                        return;
                    }
                }
            }
        }
    }

    private void sendCommand(String serverName,
            GuildMessageReceivedEvent event) throws SocketException, UnknownHostException {
        RconEntity entity = rconService.findByServerName(serverName);
        String[] serverAddress = entity.getServerAddress().split(":");
        int sizeCmdName = event.getMessage().getContentRaw().split("\\s+")[0].length() + 1;
        String message = event.getMessage().getContentRaw().substring(sizeCmdName);
        RconUtil rconUtil = new RconUtil(serverAddress[0], Integer.parseInt(serverAddress[1]), entity.getRconPassword());
        rconUtil.commandAsnc(message, response -> {
            String out = rconUtil.getNormalizedResponse(response);
            EmbedBuilder emb = RconEmbedUtil.getEmbedBuilder("rcon/" + serverName + " " + message,
                    "Rcon Execute Command",
                    out);
            event.getChannel().sendMessage(event.getAuthor().getAsMention()).queue(msg ->
                    msg.editMessageEmbeds(emb.build()).queue());
        });
    }
}
