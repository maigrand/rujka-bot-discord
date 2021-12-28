package com.maigrand.rujka.controller.discord;

import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.service.discord.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discord/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    @GetMapping
    public List<MonitoringEntity> list() {
        return monitoringService.findAll();
    }

    @GetMapping("/{id}")
    public MonitoringEntity show(@PathVariable("id") Integer id) {
        return monitoringService.findById(id);
    }
}
