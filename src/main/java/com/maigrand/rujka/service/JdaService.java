package com.maigrand.rujka.service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class JdaService {

    private JDA jda;

    @Value("${app.discord.token}")
    private String token;

    public JDA getJda() {
        if (this.jda == null) {
            try {
                JDABuilder jdaBuilder = JDABuilder.createLight(token);
                jdaBuilder.disableCache(CacheFlag.ACTIVITY);
                jdaBuilder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));
                jdaBuilder.setChunkingFilter(ChunkingFilter.NONE);
                jdaBuilder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
                jdaBuilder.setLargeThreshold(50);

                this.jda = jdaBuilder.build();
            } catch (LoginException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return this.jda;
    }

    /*public JDA getJda() {
        if (this.jda == null) {
            try {
                EnumSet<GatewayIntent> gatewayIntents = EnumSet.allOf(GatewayIntent.class);
                this.jda = JDABuilder.create(token, gatewayIntents).build();
            } catch (LoginException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return this.jda;
    }*/
}
