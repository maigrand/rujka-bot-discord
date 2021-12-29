package com.maigrand.rujka.discord.monitoringmodule.action;

import com.maigrand.rujka.discord.util.InfoEmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MonitoringInfoAction {

    public void execute(GuildMessageReceivedEvent event) {
        String s = "Основные команды:" +
                "\n" +
                "`m/list`" +
                "\n" +
                "`m/add`" +
                "\n" +
                "`m/edit`" +
                "\n" +
                "`m/remove`";
        EmbedBuilder emb = InfoEmbedUtil.getEmbedBuilder(event.getJDA(), "Monitoring Module", s);
        event.getChannel().sendMessageEmbeds(emb.build()).queue();
    }
}
