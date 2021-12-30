package com.maigrand.rujka.discord.monitoringmodule.action;

import com.maigrand.rujka.discord.util.DiscordId;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

@RequiredArgsConstructor
public class MonitoringAddAction {

    private final MonitoringService monitoringService;

    public void execute(MessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length < 5) {
            event.getChannel().sendMessage(
                    "Usage: cmd <#textChannel> <serverIp:port> <serverName> <password or null> [serverIndex]" +
                            "\n `m/add #mon 127.0.0.1:29071 eslhome esl`" +
                            "\n `m/add #membermon 127.0.0.1:29070 eslmain null 1`"
            ).queue();
            return;
        }

        TextChannel textChannel = DiscordId.getTextChannel(event.getGuild(), args[1]);

        if (textChannel == null) {
            event.getChannel().sendMessage("Bad textChannel").queue();
            return;
        }

        if (!args[2].contains(":")) {
            event.getChannel().sendMessage("bad port").queue();
            return;
        }

        int serverIndex;
        List<MonitoringEntity> monitoringEntityList = monitoringService.findByChannelId(textChannel.getId());

        if (args.length == 6) {
            serverIndex = Integer.parseInt(args[5]);
        } else {
            serverIndex = monitoringEntityList.size();
        }

        Message msg = textChannel.sendMessageEmbeds(new EmbedBuilder().setTitle(args[3]).build()).complete();

        MonitoringEntity entity = new MonitoringEntity();
        entity.setGuildId(msg.getGuild().getId());
        entity.setChannelId(textChannel.getId());
        entity.setServerAddress(args[2]);
        entity.setServerName(args[3]);
        entity.setServerPassword(args[4]);
        entity.setMessageId(msg.getId());
        entity.setServerIndex(serverIndex);
        monitoringEntityList.add(serverIndex, entity);

        try {
            monitoringService.deleteAllByChannelId(textChannel.getId());

            for (int i = 0; i < monitoringEntityList.size(); i++) {
                monitoringEntityList.get(i).setServerIndex(i);
                monitoringService.save(monitoringEntityList.get(i));
            }
            event.getChannel().sendMessage("Server \"" + args[3] + "\" (" + args[2] +
                    ") has been added successfully" +
                    " into " + textChannel.getAsMention() + " (pw:" + args[4] + ")").queue();
        } catch (Exception e) {
            e.printStackTrace();
            msg.delete().queue();
            msg.getChannel().sendMessage(e.getLocalizedMessage()).queue();
        }
    }
}
