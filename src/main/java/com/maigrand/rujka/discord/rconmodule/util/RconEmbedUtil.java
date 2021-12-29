package com.maigrand.rujka.discord.rconmodule.util;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class RconEmbedUtil {

    public static EmbedBuilder getEmbedBuilder(String cmd, String title, String description) {
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor(cmd);
        emb.setTitle(title);
        emb.setDescription(description);
        emb.setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()));
        emb.setColor(new Color(255, 165, 0));
        return emb;
    }
}
