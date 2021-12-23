package com.maigrand.rujka.service.discord.rcon;

import com.maigrand.rujka.entity.discord.rcon.RconIpScanIpEntity;
import com.maigrand.rujka.repository.discord.rcon.RconIpScanIpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class RconIpScanIpService {

    private final RconIpScanIpRepository rconIpScanIpRepository;

    public RconIpScanIpEntity findByIp(String ip) {
        return rconIpScanIpRepository.findByIp(ip)
                .orElse(null);
    }

    public RconIpScanIpEntity create(String ip) {
        RconIpScanIpEntity ipEntity = findByIp(ip);
        if (ipEntity != null) {
            return ipEntity;
        }
        ipEntity = new RconIpScanIpEntity();
        ipEntity.setIp(ip);
        return rconIpScanIpRepository.save(ipEntity);
    }
}
