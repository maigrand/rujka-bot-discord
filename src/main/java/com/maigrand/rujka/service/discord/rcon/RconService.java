package com.maigrand.rujka.service.discord.rcon;

import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import com.maigrand.rujka.repository.discord.rcon.RconRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class RconService {

    private final RconRepository rconRepository;

    public RconEntity findById(int id) {
        return rconRepository.findById(id)
                .orElse(null);
    }

    public List<RconEntity> findAll() {
        return rconRepository.findAll();
    }

    public RconEntity findByServerName(String serverName) {
        return rconRepository.findByServerName(serverName)
                .orElse(null);
    }

    public RconEntity findByServerNameOrId(String arg) {
        RconEntity entity;
        try {
            int i = Integer.parseInt(arg);
            entity = findById(i);
        } catch (NumberFormatException e) {
            entity = findByServerName(arg);
        }
        return entity;
    }

    public RconEntity save(RconEntity entity) {
        return rconRepository.save(entity);
    }

    public void delete(RconEntity entity) {
        rconRepository.delete(entity);
    }
}
