package com.maigrand.rujka.discord.messageloggingmodule.util;

import com.maigrand.rujka.entity.discord.MessageStoreEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MessageLoggingEmbedUtil {

    public static EmbedBuilder getEmbedBuilder(MessageStoreEntity entity,
            MessageLoggingType type,
            Guild guild,
            String newText) {
        EmbedBuilder emb = new EmbedBuilder();

        Member member = guild.getMemberById(entity.getUserId());
        TextChannel textChannel = guild.getTextChannelById(entity.getChannelId());

        emb.setAuthor(member.getEffectiveName(), null, member.getUser().getEffectiveAvatarUrl());
        emb.setColor(new Color(255, 165, 0));
        emb.setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()));
        emb.addField("Channel", textChannel.getAsMention(), false);

        switch (type) {
            case UPDATE:
                emb.setTitle("Edited message");
                emb.addField("Old Content", entity.getText(), false);
                emb.addField("New Content", newText, false);
                break;
            case DELETE:
                emb.setTitle("Removed message");
                emb.addField("Content", entity.getText(), false);
        }

        return emb;
    }
}
