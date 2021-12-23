package com.maigrand.rujka.repository.discord.rcon;

import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RconRepository extends JpaRepository<RconEntity, Integer> {

    Optional<RconEntity> findByServerName(String serverName);
}
