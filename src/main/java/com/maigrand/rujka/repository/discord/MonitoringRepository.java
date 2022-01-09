package com.maigrand.rujka.repository.discord;

import com.maigrand.rujka.entity.discord.MonitoringEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitoringRepository extends JpaRepository<MonitoringEntity, Integer> {

    List<MonitoringEntity> findByChannelId(String channelId);

    Optional<MonitoringEntity> findByChannelIdAndServerName(String channelId, String serverName);

    List<MonitoringEntity> findByGuildId(String guildId);

    void deleteAllByChannelId(String channelId);
}
