package com.maigrand.rujka.service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

@Service
public class JdaService {

    private JDA jda;

    @Value("${app.discord.token}")
    private String token;

    public JDA getJda() {
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
    }
}
