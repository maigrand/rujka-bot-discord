package com.maigrand.rujka.discord.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class InfoEmbedUtil {

    public static EmbedBuilder getEmbedBuilder(JDA jda, String title, String description) {
        EmbedBuilder emb = new EmbedBuilder();
        //mai (author) id
        User userById = jda.getUserById("154437997989855232");
        emb.setAuthor(userById.getName(),
                "https://github.com/mairontai/rujka-bot-discord",
                userById.getEffectiveAvatarUrl());
        emb.setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()));
        emb.setTitle(title);
        emb.setDescription(description);
        emb.setThumbnail(userById.getEffectiveAvatarUrl());
        emb.setColor(new Color(110,0,250));
        return emb;
    }
}
