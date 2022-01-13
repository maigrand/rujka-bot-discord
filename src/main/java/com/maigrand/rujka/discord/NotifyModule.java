package com.maigrand.rujka.discord;

import com.maigrand.rujka.service.JdaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class NotifyModule {

    private final JdaService jdaService;

    @Value("${app.discord.owner_id}")
    private String ownerId;

    public void sendMessage(String message) {
        jdaService.getJda().getUserById(ownerId).openPrivateChannel().queue(pc -> {
            pc.sendMessage(message).queue();
        }, throwable -> {
            System.out.println(Arrays.toString(throwable.getStackTrace()));
        });
    }
}
