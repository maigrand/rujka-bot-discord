package com.maigrand.rujka.discord.rconmodule.action;

import com.maigrand.rujka.discord.rconmodule.util.RconEmbedUtil;
import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import com.maigrand.rujka.service.discord.rcon.RconService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

@RequiredArgsConstructor
public class RconListAction {

    private final RconService rconService;

    public void execute(GuildMessageReceivedEvent event) {
        List<RconEntity> all = rconService.findAll();
        StringBuilder sb = new StringBuilder();
        for (RconEntity ent : all) {
            sb.append(ent.getId()).append(") ").append(ent.getServerName()).append(" ").append(ent.getServerAddress())
                    .append(" ").append(ent.getRconPassword()).append("\n");
        }

        EmbedBuilder emb = RconEmbedUtil.getEmbedBuilder("rcon/list",
                "Rcon Manager Server List",
                sb.toString());

        event.getChannel().sendMessage(event.getAuthor().getAsMention()).queue(msg ->
                msg.editMessageEmbeds(emb.build()).queue());
    }
}
