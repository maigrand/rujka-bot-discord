package com.maigrand.rujka.service.discord;

import com.maigrand.rujka.entity.discord.MonitoringEntity;
import com.maigrand.rujka.payload.PaginationDetails;
import com.maigrand.rujka.payload.discord.MonitoringDetails;
import com.maigrand.rujka.repository.discord.MonitoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final MonitoringRepository monitoringRepository;

    public List<MonitoringEntity> findAll() {
        return this.monitoringRepository.findAll();
    }

    public PaginationDetails<MonitoringEntity> findAll(Pageable pageable) {
        return new PaginationDetails<>(monitoringRepository.findAll(pageable));
    }

    public List<MonitoringEntity> findByGuildId(String id) {
        return this.monitoringRepository.findByGuildId(id);
    }

    public List<MonitoringEntity> findByChannelId(String id) {
        return this.monitoringRepository.findByChannelId(id);
    }

    public MonitoringEntity findByChannelIdAndServerName(String channelId, String serverName) {
        return monitoringRepository.findByChannelIdAndServerName(channelId, serverName)
                .orElse(null);
    }

    public int countAll() {
        return this.monitoringRepository.countAll();
    }

    public MonitoringEntity save(MonitoringDetails details) {
        MonitoringEntity monitoringEntity = new MonitoringEntity();
        monitoringEntity.setGuildId(details.getGuildId());
        monitoringEntity.setChannelId(details.getChannelId());
        monitoringEntity.setServerAddress(details.getServerAddress());
        monitoringEntity.setServerName(details.getServerName());
        monitoringEntity.setServerPassword(details.getServerPassword());
        monitoringEntity.setServerIndex(details.getServerIndex());
        //todo: добавить кучу проверок

        return this.monitoringRepository.save(monitoringEntity);
    }

    public MonitoringEntity save(MonitoringEntity monitoringEntity) {
        return this.monitoringRepository.save(monitoringEntity);
    }

    @Transactional
    public void deleteAllByChannelId(String channelId) {
        this.monitoringRepository.deleteAllByChannelId(channelId);
    }

    public void delete(int id) {
        monitoringRepository.deleteById(id);
    }

    public MonitoringEntity findById(Integer id) {
        return monitoringRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("monitoring not found"));
    }
}
