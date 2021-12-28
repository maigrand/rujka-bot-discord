package com.maigrand.rujka.discord.monitoringmodule.action;

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

        event.getChannel().sendMessage(s).queue();
    }
}
