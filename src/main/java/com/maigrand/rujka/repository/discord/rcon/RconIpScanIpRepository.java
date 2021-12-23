package com.maigrand.rujka.repository.discord.rcon;

import com.maigrand.rujka.entity.discord.rcon.RconIpScanIpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RconIpScanIpRepository extends JpaRepository<RconIpScanIpEntity, Integer> {

    Optional<RconIpScanIpEntity> findByIp(String ip);
}
