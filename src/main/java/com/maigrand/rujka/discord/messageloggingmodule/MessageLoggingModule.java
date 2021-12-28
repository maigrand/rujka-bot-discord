package com.maigrand.rujka.discord.messageloggingmodule;

import com.maigrand.rujka.discord.DiscordModule;
import com.maigrand.rujka.discord.messageloggingmodule.util.MessageLoggingEmbedUtil;
import com.maigrand.rujka.discord.messageloggingmodule.util.MessageLoggingType;
import com.maigrand.rujka.entity.discord.MessageStoreEntity;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.MessageStoreService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class MessageLoggingModule extends DiscordModule {

    private final MessageStoreService messageStoreService;

    private TextChannel logTextChannel;

    //todo: вынести в бд
    @Value("${app.discord.module.message_logging.text_channel_id}")
    private String LOG_CHANNEL_ID;

    public MessageLoggingModule(JdaService jdaService,
            MessageStoreService messageStoreService) {
        super(jdaService);
        this.messageStoreService = messageStoreService;
    }

    @PostConstruct
    public void init() {
        removeOldMessages();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        if (event.getAuthor().isBot()) {
            return;
        }

        messageSave(event);
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        super.onGuildMessageDelete(event);

        messageDelete(event);
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        super.onGuildMessageUpdate(event);

        messageUpdate(event);
    }

    private void messageSave(GuildMessageReceivedEvent event) {
        MessageStoreEntity ent = new MessageStoreEntity();
        ent.setGuildId(event.getGuild().getId());
        ent.setChannelId(event.getChannel().getId());
        ent.setMessageId(event.getMessageId());
        ent.setUserId(event.getAuthor().getId());
        ent.setText(event.getMessage().getContentRaw());

        messageStoreService.save(ent);
    }

    private void messageUpdate(GuildMessageUpdateEvent event) {
        Guild guild = event.getGuild();
        String guildId = guild.getId();
        String channelId = event.getChannel().getId();
        String messageId = event.getMessage().getId();
        String text = event.getMessage().getContentRaw();

        MessageStoreEntity ent = messageStoreService
                .findByGuildIdAndChannelIdAndMessageId(guildId, channelId, messageId);
        if (ent == null) {
            return;
        }

        EmbedBuilder embedBuilder = MessageLoggingEmbedUtil.getEmbedBuilder(ent,
                MessageLoggingType.UPDATE,
                guild,
                text);
        TextChannel logTextChannel = getLogTextChannel(guild);

        logTextChannel.sendMessageEmbeds(embedBuilder.build()).queue();

        ent.setText(text);
        messageStoreService.save(ent);
    }

    private void messageDelete(GuildMessageDeleteEvent event) {
        Guild guild = event.getGuild();
        String guildId = guild.getId();
        String channelId = event.getChannel().getId();
        String messageId = event.getMessageId();

        MessageStoreEntity ent = messageStoreService
                .findByGuildIdAndChannelIdAndMessageId(guildId, channelId, messageId);
        if (ent == null) {
            return;
        }

        EmbedBuilder embedBuilder = MessageLoggingEmbedUtil.getEmbedBuilder(ent,
                MessageLoggingType.DELETE,
                guild,
                null);
        TextChannel logTextChannel = getLogTextChannel(guild);

        logTextChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        messageStoreService.delete(ent);
    }

    private TextChannel getLogTextChannel(Guild guild) {
        if (logTextChannel == null) {
            logTextChannel = guild.getTextChannelById(LOG_CHANNEL_ID);
        }
        return logTextChannel;
    }

    private void removeOldMessages() {
        for (MessageStoreEntity ent : messageStoreService.findAll()) {
            //LocalDate localDate = ent.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            //int entDay = localDate.getDayOfYear();
            int entDay = ent.getCreatedAt().toLocalDateTime().getDayOfYear();
            int nowDay = LocalDateTime.now().getDayOfYear();
            if (nowDay - entDay >= 7) {
                messageStoreService.delete(ent);
            }
        }
    }
}
