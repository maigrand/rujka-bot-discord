package com.maigrand.rujka.discord.rconmodule.action;

import com.maigrand.rujka.discord.util.InfoEmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RconInfoAction {

    public void execute(MessageReceivedEvent event) {
        String s = "Основные команды (будут работать только в специальном канале):" +
                "\n" +
                "`rcon/add`" +
                "\n" +
                "`rcon/remove`" +
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

        EmbedBuilder emb = InfoEmbedUtil.getEmbedBuilder(event.getJDA(), "Rcon Module", s);
        event.getChannel().sendMessageEmbeds(emb.build()).queue();
    }
}
