package com.maigrand.rujka.discord;

import com.maigrand.rujka.service.JdaService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class DiscordModule extends ListenerAdapter {

    protected final JdaService jdaService;

    public DiscordModule(JdaService jdaService) {
        this.jdaService = jdaService;
        JDA jda = jdaService.getJda();
        jda.addEventListener(this);
    }
}
