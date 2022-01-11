package com.maigrand.rujka.controller.discord;

import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.payload.PaginationDetails;
import com.maigrand.rujka.payload.discord.MonitoringDetails;
import com.maigrand.rujka.service.discord.MonitoringService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/discord/monitoring")
@Api(tags = "Мониторинг")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    //todo: search criteria
    @GetMapping
    @ApiOperation(value = "Получить список мониторингов")
    public PaginationDetails<MonitoringEntity> list(Pageable pageable) {
        return monitoringService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Получить мониторинг по ID")
    public MonitoringEntity show(@PathVariable("id") Integer id) {
        return monitoringService.findById(id);
    }

    @PostMapping
    @ApiOperation(value = "Создать мониторинг")
    public MonitoringEntity create(@RequestBody MonitoringDetails details){
        return monitoringService.save(details);
    }
}
