package com.maigrand.rujka.discord.stuffmodule;

import com.maigrand.rujka.discord.DiscordModule;
import com.maigrand.rujka.discord.NotifyModule;
import com.maigrand.rujka.discord.util.InfoEmbedUtil;
import com.maigrand.rujka.service.JdaService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class StuffModule extends DiscordModule {

    private final NotifyModule notifyModule;

    public StuffModule(JdaService jdaService, NotifyModule notifyModule) {
        super(jdaService);
        this.notifyModule = notifyModule;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (event.getMessage().getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().getMentionedMembers().contains(event.getGuild().getMember(event.getJDA().getSelfUser()))) {
            String s = "Мониторинг: m/help\nРкон: rcon/help\nПрава: perm/help";
            EmbedBuilder emb = InfoEmbedUtil.getEmbedBuilder(event.getJDA(), "Доступные модули", s);
            //todo: Дополнить механизм дискорд кнопок
            /* ActionRow actionRow = ActionRow.of(List.of(Button.primary("permission", "permission"),
                    Button.primary("monitoring", "monitoring"),
                    Button.primary("rcon", "rcon")));
            event.getChannel().sendMessageEmbeds(emb.build())
                    .setActionRows(actionRow)
                    .queue();*/
            event.getChannel().sendMessageEmbeds(emb.build()).queue();
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        for (String arg : args) {
            if (arg.equals("mai") || arg.equals("май") || arg.equals("маю") || arg.equals("мая")) {
                notifyModule.sendMessage("Mentioned "
                        + event.getMessage().getJumpUrl()
                        + " with text: "
                        + event.getMessage().getContentRaw());
            }
        }
    }

    /*@Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        super.onButtonClick(event);
        if (event.getComponentId().equals("rcon")) {
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
            event.getMessage().editMessageEmbeds(emb.build()).queue();
            event.deferEdit().queue();
        }
    }*/
}
