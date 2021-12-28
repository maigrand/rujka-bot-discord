package com.maigrand.rujka.service.discord;

import com.maigrand.rujka.entity.discord.PermissionEntity;
import com.maigrand.rujka.repository.discord.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<PermissionEntity> findAll() {
        return permissionRepository.findAll();
    }

    public PermissionEntity findByName(String name) {
        return permissionRepository.findByName(name)
                .orElse(null);
    }

    public PermissionEntity save(PermissionEntity entity) {
        return permissionRepository.save(entity);
    }
}
