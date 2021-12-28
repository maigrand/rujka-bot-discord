package com.maigrand.rujka.service.discord;

import com.maigrand.rujka.entity.discord.RoleEntity;
import com.maigrand.rujka.repository.discord.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    public RoleEntity findByRoleIdAndGuildId(String roleId, String guildId) {
        return roleRepository.findByRoleIdAndGuildId(roleId, guildId)
                .orElse(null);
    }

    public RoleEntity save(RoleEntity entity) {
        return roleRepository.save(entity);
    }

    public List<RoleEntity> findAllByGuildId(String guildId) {
        return roleRepository.findAllByGuildId(guildId);
    }
}
