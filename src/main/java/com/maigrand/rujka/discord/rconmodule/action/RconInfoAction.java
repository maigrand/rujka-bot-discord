package com.maigrand.rujka.discord.rconmodule.action;

import com.maigrand.rujka.discord.rconmodule.util.RconEmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RconInfoAction {

    public void execute(GuildMessageReceivedEvent event) {
        String s = "Основные команды (будут работать только в специальном канале):" +
                "\n" +
                "`rcon/add`" +
                "\n" +
                "`rcon/rem`" +
                "\n" +
                "`rcon/edit`" +
                "\n" +
                "`rcon/list`" +
                "\n" +
                "Ко всем командам, кроме `rcon/list` на данный момент есть доступ только у Мая и Трея " +
                "(в связи с непреодолимой силой автора)." +
                "\n" +
                "Ркон-лист показывает, с какими серверами можно взаимодействовать." +
                "\n" +
                "Взаимодействие производится префиксом `rcon/` и названием сервера из ркон-листа " +
                "(пример: `rcon/rujka1 g_password maithesyberian)`." +
                "\n" +
                "Все пожелания или сообщения об ошибках писать в ЛС - <@154437997989855232>";

        EmbedBuilder emb = RconEmbedUtil.getEmbedBuilder("rcon/info", "Rcon Module Info", s);
        event.getChannel().sendMessage(event.getAuthor().getAsMention()).queue(message ->
                message.editMessageEmbeds(emb.build()).queue());
    }
}
