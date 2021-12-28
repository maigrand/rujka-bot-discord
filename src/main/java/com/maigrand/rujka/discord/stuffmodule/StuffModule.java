package com.maigrand.rujka.discord.stuffmodule;

import com.maigrand.rujka.discord.DiscordModule;
import com.maigrand.rujka.service.JdaService;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class StuffModule extends DiscordModule {

    public StuffModule(JdaService jdaService) {
        super(jdaService);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        if (event.getMessage().getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().getMentionedMembers().contains(event.getGuild().getMember(event.getJDA().getSelfUser()))) {
            event.getChannel()
                    .sendMessage("Доступные модули \n Мониторинг: m/help \n Ркон: rcon/help \n Права: perm/help")
                    .queue();
            return;
        }

        if (event.getMessage().getContentRaw().contains("май") || event.getMessage().getContentRaw().contains("mai")) {
            if (event.getMessage().getAuthor().isBot()) {
                return;
            }
            event.getJDA().getUserById("154437997989855232").openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("Mentioned " + event.getMessage().getJumpUrl()).queue();
            }, throwable -> {
                System.out.println(throwable.getMessage());
            });
        }
    }
}
