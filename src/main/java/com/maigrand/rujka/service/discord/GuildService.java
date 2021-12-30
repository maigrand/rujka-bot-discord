package com.maigrand.rujka.service.discord;

import com.maigrand.rujka.model.GuildModel;
import com.maigrand.rujka.payload.PaginationDetails;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.util.PageUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class GuildService implements PageUtil {

    private final JdaService jdaService;

    public PaginationDetails<GuildModel> findAll(Pageable pageable) {
        JDA jda = this.jdaService.getJda();
        List<GuildModel> list = new ArrayList<>();
        for (Guild guild : jda.getGuilds()) {
            GuildModel guildModel = discordGuildToGuildModel(guild);
            list.add(guildModel);
        }

        Page<GuildModel> page = (Page<GuildModel>) toPage(list, pageable);
        return new PaginationDetails<>(page);
    }

    public GuildModel findByDiscordId(String discordId) {
        JDA jda = this.jdaService.getJda();
        Guild guild = jda.getGuildById(discordId);
        return discordGuildToGuildModel(guild);
    }

    private GuildModel discordGuildToGuildModel(Guild guild) {
        GuildModel guildModel = new GuildModel();
        guildModel.setId(guild.getId());
        guildModel.setName(guild.getName());
        guildModel.setIconUrl(guild.getIconUrl());
        return guildModel;
    }
}
