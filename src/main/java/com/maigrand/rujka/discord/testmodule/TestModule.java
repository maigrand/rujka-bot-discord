package com.maigrand.rujka.discord.testmodule;

import com.maigrand.rujka.discord.DiscordModule;
import com.maigrand.rujka.service.JdaService;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class TestModule extends DiscordModule {

    public TestModule(JdaService jdaService) {
        super(jdaService);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        event.getJDA().getGuilds().forEach(System.out::println);
    }
}
