package com.maigrand.rujka.repository.discord.rcon;

import com.maigrand.rujka.entity.discord.rcon.RconIpScanNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RconIpScanNameRepository extends JpaRepository<RconIpScanNameEntity, Integer> {

    Optional<RconIpScanNameEntity> findByName(String name);

    List<RconIpScanNameEntity> findByNameContains(String name);
}
