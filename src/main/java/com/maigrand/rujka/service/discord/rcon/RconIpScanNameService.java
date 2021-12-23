package com.maigrand.rujka.service.discord.rcon;

import com.maigrand.rujka.entity.discord.rcon.RconIpScanIpEntity;
import com.maigrand.rujka.entity.discord.rcon.RconIpScanNameEntity;
import com.maigrand.rujka.repository.discord.rcon.RconIpScanNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class RconIpScanNameService {

    private final RconIpScanNameRepository rconIpScanNameRepository;

    public List<RconIpScanNameEntity> findAll() {
        return rconIpScanNameRepository.findAll();
    }

    public RconIpScanNameEntity findByName(String name) {
        return rconIpScanNameRepository.findByName(name)
                .orElse(null);
    }

    public List<RconIpScanNameEntity> findByNameContains(String name) {
        return rconIpScanNameRepository.findByNameContains(name);
    }

    public RconIpScanNameEntity create(String name, RconIpScanIpEntity ipEntity) {
        RconIpScanNameEntity entity = findByName(name);
        if (entity != null) {
            entity.addRconIpScanIpEntity(ipEntity);
            return rconIpScanNameRepository.save(entity);
        }
        entity = new RconIpScanNameEntity();
        entity.setName(name);
        entity.addRconIpScanIpEntity(ipEntity);
        return rconIpScanNameRepository.save(entity);
    }
}
